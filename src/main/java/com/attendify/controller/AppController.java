package com.attendify.controller;
import com.attendify.domain.entity.*;
import com.attendify.domain.enums.*;
import com.attendify.repository.*;
import com.attendify.security.AttendifyUserPrincipal;
import com.attendify.service.EmailService;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.SecureRandom;
import java.time.*;
import java.util.*;

@Controller
public class AppController {
    private static final SecureRandom RNG = new SecureRandom();
    private final UserRepository users; private final OtpVerificationRepository otps;
    private final AcademicClassRepository classes; private final StudentRepository students;
    private final SubjectRepository subjects; private final AttendanceSessionRepository sessions;
    private final AttendanceRecordRepository records; private final PasswordEncoder encoder;
    private final EmailService emailService; private final SecurityContextRepository secRepo;

    public AppController(UserRepository users, OtpVerificationRepository otps, AcademicClassRepository classes, StudentRepository students, SubjectRepository subjects, AttendanceSessionRepository sessions, AttendanceRecordRepository records, PasswordEncoder encoder, EmailService emailService, SecurityContextRepository secRepo) {
        this.users = users; this.otps = otps; this.classes = classes; this.students = students; this.subjects = subjects; this.sessions = sessions; this.records = records; this.encoder = encoder; this.emailService = emailService; this.secRepo = secRepo;
    }

    @GetMapping("/login") public String loginPage() { return "auth/login"; }

    @PostMapping("/login") @Transactional
    public String loginPost(@RequestParam String email, @RequestParam String password, HttpSession session, RedirectAttributes ra) {
        Optional<User> uOpt = users.findByEmailIgnoreCase(email.trim());
        if (uOpt.isEmpty() || !uOpt.get().isActive() || !encoder.matches(password, uOpt.get().getPasswordHash())) {
            ra.addFlashAttribute("error", "Invalid email or password."); return "redirect:/login";
        }
        User u = uOpt.get();
        String rawOtp = String.format("%06d", RNG.nextInt(1_000_000));
        String attemptId = UUID.randomUUID().toString();
        OtpVerification otp = new OtpVerification();
        otp.setUser(u); otp.setLoginAttemptId(attemptId); otp.setOtpHash(encoder.encode(rawOtp));
        otp.setExpiresAt(Instant.now().plusSeconds(300));
        otps.save(otp);
        try {
            emailService.sendOtp(u.getEmail(), u.getFullName(), rawOtp);
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage()); return "redirect:/login";
        }
        session.setAttribute("OTP_USER", u.getId()); session.setAttribute("OTP_ATTEMPT", attemptId); session.setAttribute("OTP_EMAIL", u.getEmail());
        return "redirect:/verify-otp";
    }

    @GetMapping("/verify-otp")
    public String verifyPage(HttpSession session, Model m) {
        if (session.getAttribute("OTP_USER") == null) return "redirect:/login";
        m.addAttribute("email", session.getAttribute("OTP_EMAIL"));
        return "auth/verify-otp";
    }

    @PostMapping("/verify-otp") @Transactional
    public String verifyPost(@RequestParam String otpCode, HttpServletRequest req, HttpServletResponse res, RedirectAttributes ra) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("OTP_USER") == null) return "redirect:/login";
        User u = users.findById((Long) session.getAttribute("OTP_USER")).orElseThrow();
        OtpVerification otp = otps.findByUserAndLoginAttemptId(u, (String) session.getAttribute("OTP_ATTEMPT")).orElseThrow();
        if (!otp.isUsable(Instant.now()) || !encoder.matches(otpCode.trim(), otp.getOtpHash())) {
            otp.setFailedAttemptCount(otp.getFailedAttemptCount() + 1); otps.save(otp);
            ra.addFlashAttribute("error", "Invalid or expired OTP code."); return "redirect:/verify-otp";
        }
        otp.setConsumedAt(Instant.now()); otp.setInvalidated(true); otps.save(otp);
        session.removeAttribute("OTP_USER"); session.removeAttribute("OTP_ATTEMPT");
        AttendifyUserPrincipal p = new AttendifyUserPrincipal(u);
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(new UsernamePasswordAuthenticationToken(p, null, p.getAuthorities()));
        SecurityContextHolder.setContext(ctx); secRepo.saveContext(ctx, req, res);
        return "redirect:/dashboard";
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(@AuthenticationPrincipal AttendifyUserPrincipal user, Model m) {
        m.addAttribute("user", user);
        m.addAttribute("studentCount", students.count());
        m.addAttribute("classCount", classes.count());
        m.addAttribute("presentCount", records.countByAttendanceStatus(AttendanceStatus.PRESENT));
        m.addAttribute("absentCount", records.countByAttendanceStatus(AttendanceStatus.ABSENT));
        m.addAttribute("classes", classes.findByActiveTrueOrderByClassNameAsc());
        m.addAttribute("subjects", subjects.findByActiveTrueOrderBySubjectNameAsc());
        m.addAttribute("students", students.findAllByOrderByRegistrationNumberAsc());
        m.addAttribute("sessions", sessions.findTop10ByOrderByAttendanceDateDesc());
        m.addAttribute("today", LocalDate.now());
        return "dashboard/index";
    }

    @PostMapping("/students/add") @Transactional
    public String addStudent(@RequestParam String studentName, @RequestParam String registrationNumber, @RequestParam Long classId, RedirectAttributes ra) {
        Student s = new Student(); s.setStudentName(studentName.trim()); s.setRegistrationNumber(registrationNumber.trim().toUpperCase());
        s.setAcademicClass(classes.findById(classId).orElseThrow()); students.save(s);
        ra.addFlashAttribute("msg", "Student enrolled!"); return "redirect:/dashboard";
    }

    @PostMapping("/classes/add") @Transactional
    public String addClass(@RequestParam String className, @RequestParam String sectionName, @RequestParam String academicYear, RedirectAttributes ra) {
        AcademicClass c = new AcademicClass(); c.setClassName(className); c.setSectionName(sectionName); c.setAcademicYear(academicYear); classes.save(c);
        ra.addFlashAttribute("msg", "Class created!"); return "redirect:/dashboard";
    }

    @PostMapping("/attendance/mark") @Transactional
    public String markAttendance(@AuthenticationPrincipal AttendifyUserPrincipal user, @RequestParam Long classId, @RequestParam Long subjectId, @RequestParam String date, HttpServletRequest req, RedirectAttributes ra) {
        AcademicClass c = classes.findById(classId).orElseThrow();
        Subject sub = subjects.findById(subjectId).orElseThrow();
        User marker = users.findById(user.getId()).orElseThrow();
        LocalDate d = LocalDate.parse(date);
        AttendanceSession sess = sessions.findByAcademicClassIdAndSubjectIdAndAttendanceDate(classId, subjectId, d).orElseGet(() -> {
            AttendanceSession ns = new AttendanceSession(); ns.setAcademicClass(c); ns.setSubject(sub); ns.setAttendanceDate(d); ns.setCreatedBy(marker); return sessions.save(ns);
        });
        List<Student> list = students.findByAcademicClassIdAndActiveTrueOrderByRegistrationNumberAsc(classId);
        for (Student st : list) {
            String val = req.getParameter("status_" + st.getId());
            AttendanceRecord r = new AttendanceRecord();
            r.setAttendanceSession(sess); r.setStudent(st);
            r.setAttendanceStatus(val != null ? AttendanceStatus.valueOf(val) : AttendanceStatus.PRESENT);
            r.setMarkedBy(marker); records.save(r);
        }
        ra.addFlashAttribute("msg", "Attendance saved for " + list.size() + " students!");
        return "redirect:/dashboard";
    }
}
