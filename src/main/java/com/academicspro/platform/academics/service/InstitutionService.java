package com.academicspro.platform.academics.service;

import com.academicspro.platform.academics.dto.CreateCampusRequest;
import com.academicspro.platform.academics.dto.InstitutionOverviewDto;
import com.academicspro.platform.academics.model.*;
import com.academicspro.platform.tenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InstitutionService {

    private final Map<String, List<Campus>> campusStore = new ConcurrentHashMap<>();
    private final Map<String, List<AcademicYear>> academicYearStore = new ConcurrentHashMap<>();
    private final Map<String, List<ClassGrade>> classGradeStore = new ConcurrentHashMap<>();
    private final Map<String, List<Subject>> subjectStore = new ConcurrentHashMap<>();
    private final Map<String, List<Classroom>> classroomStore = new ConcurrentHashMap<>();

    public InstitutionService() {
        seedApexHigh();
        seedOakridge();
    }

    private void seedApexHigh() {
        String tenantId = "apex-high";

        // Campuses
        Campus main = Campus.builder()
                .id("cmp-apex-main")
                .tenantId(tenantId)
                .institutionId("inst-apex-high")
                .name("Main Cambridge Campus")
                .code("CAM-MAIN")
                .address("742 Evergreen Crest, Cambridge, MA 02138")
                .contactPhone("+1 (555) 382-9011")
                .contactEmail("cambridge@apexhigh.edu")
                .studentCapacity(1400)
                .gradesOffered("Pre-K through Grade 12")
                .active(true)
                .build();

        Campus annex = Campus.builder()
                .id("cmp-apex-annex")
                .tenantId(tenantId)
                .institutionId("inst-apex-high")
                .name("Apex Innovation Annex & STEM Lab")
                .code("CAM-ANNEX")
                .address("18 Tech Commons, Cambridge, MA 02139")
                .contactPhone("+1 (555) 382-9015")
                .contactEmail("innovation@apexhigh.edu")
                .studentCapacity(350)
                .gradesOffered("Grade 9–12 (Robotics & AI Atelier)")
                .active(true)
                .build();

        campusStore.put(tenantId, new ArrayList<>(List.of(main, annex)));

        // Academic Years & Terms
        AcademicTerm term1 = AcademicTerm.builder()
                .id("trm-ay2627-1")
                .termName("Fall Term 1")
                .termNumber(1)
                .startDate(LocalDate.of(2026, 8, 25))
                .endDate(LocalDate.of(2026, 12, 18))
                .examStartDate(LocalDate.of(2026, 12, 10))
                .examEndDate(LocalDate.of(2026, 12, 17))
                .status("CURRENT")
                .build();

        AcademicTerm term2 = AcademicTerm.builder()
                .id("trm-ay2627-2")
                .termName("Spring Term 2")
                .termNumber(2)
                .startDate(LocalDate.of(2027, 1, 12))
                .endDate(LocalDate.of(2027, 5, 28))
                .examStartDate(LocalDate.of(2027, 5, 18))
                .examEndDate(LocalDate.of(2027, 5, 26))
                .status("UPCOMING")
                .build();

        AcademicYear ay = AcademicYear.builder()
                .id("ay-2026-2027")
                .tenantId(tenantId)
                .name("Academic Year 2026–2027")
                .code("AY2627")
                .startDate(LocalDate.of(2026, 8, 25))
                .endDate(LocalDate.of(2027, 5, 28))
                .status("CURRENT")
                .terms(new ArrayList<>(List.of(term1, term2)))
                .build();

        academicYearStore.put(tenantId, new ArrayList<>(List.of(ay)));

        // Classes & Sections
        ClassGrade g10 = ClassGrade.builder()
                .id("cls-g10")
                .tenantId(tenantId)
                .name("Grade 10")
                .stage("Middle/Upper")
                .displayOrder(10)
                .sections(new ArrayList<>(List.of(
                        Section.builder().id("sec-10a").classGradeId("cls-g10").name("A").roomNumber("Room 201").maxCapacity(25).currentEnrollment(24).classTeacherName("Marcus Brody").build(),
                        Section.builder().id("sec-10b").classGradeId("cls-g10").name("B").roomNumber("Room 202").maxCapacity(25).currentEnrollment(23).classTeacherName("Liam O'Connor").build()
                )))
                .build();

        ClassGrade g11 = ClassGrade.builder()
                .id("cls-g11")
                .tenantId(tenantId)
                .name("Grade 11")
                .stage("Senior (IB DP / AP)")
                .displayOrder(11)
                .sections(new ArrayList<>(List.of(
                        Section.builder().id("sec-11a").classGradeId("cls-g11").name("A").roomNumber("Room 301").maxCapacity(25).currentEnrollment(25).classTeacherName("Sarah Lin").build(),
                        Section.builder().id("sec-11b").classGradeId("cls-g11").name("B").roomNumber("Room 302").maxCapacity(25).currentEnrollment(24).classTeacherName("Dr. Alistair Cook").build()
                )))
                .build();

        ClassGrade g12 = ClassGrade.builder()
                .id("cls-g12")
                .tenantId(tenantId)
                .name("Grade 12")
                .stage("Senior (IB DP / AP)")
                .displayOrder(12)
                .sections(new ArrayList<>(List.of(
                        Section.builder().id("sec-12a").classGradeId("cls-g12").name("A").roomNumber("Room 401").maxCapacity(25).currentEnrollment(22).classTeacherName("Dr. Arthur Vance").build()
                )))
                .build();

        classGradeStore.put(tenantId, new ArrayList<>(List.of(g10, g11, g12)));

        // Subjects
        subjectStore.put(tenantId, new ArrayList<>(List.of(
                Subject.builder().id("sbj-math11").tenantId(tenantId).code("MATH-BC").name("Calculus BC (AP)").curriculum("AP").department("Mathematics").credits(4).weeklyHours(5).build(),
                Subject.builder().id("sbj-phys11").tenantId(tenantId).code("IB-PHYS-HL").name("Physics Higher Level").curriculum("IB DP").department("Physical Sciences").credits(4).weeklyHours(6).build(),
                Subject.builder().id("sbj-chem11").tenantId(tenantId).code("IB-CHEM-HL").name("Chemistry Higher Level").curriculum("IB DP").department("Physical Sciences").credits(4).weeklyHours(5).build(),
                Subject.builder().id("sbj-lit11").tenantId(tenantId).code("ENG-LIT-AP").name("English Literature & Composition").curriculum("AP").department("Humanities").credits(3).weeklyHours(4).build(),
                Subject.builder().id("sbj-cs11").tenantId(tenantId).code("STEM-AI").name("Applied Robotics & Machine Intelligence").curriculum("STEM Atelier").department("Computer Science").credits(4).weeklyHours(5).build()
        )));

        // Classrooms
        classroomStore.put(tenantId, new ArrayList<>(List.of(
                Classroom.builder().id("rm-301").tenantId(tenantId).campusId("cmp-apex-main").roomNumber("Lab 301").name("Calculus & Advanced Analytics Lab").roomType("LABORATORY").capacity(28).build(),
                Classroom.builder().id("rm-phys1").tenantId(tenantId).campusId("cmp-apex-main").roomNumber("Phys Lab 1").name("Newtonian & Quantum Mechanics Atelier").roomType("LABORATORY").capacity(26).build(),
                Classroom.builder().id("rm-hallb").tenantId(tenantId).campusId("cmp-apex-main").roomNumber("Hall B").name("Humanities Lecture Hall B").roomType("LECTURE_HALL").capacity(60).build(),
                Classroom.builder().id("rm-turing").tenantId(tenantId).campusId("cmp-apex-annex").roomNumber("Turing Lab").name("Turing AI & Prototyping Workshop").roomType("LABORATORY").capacity(32).build()
        )));
    }

    private void seedOakridge() {
        String tenantId = "oakridge-academy";
        Campus oakMain = Campus.builder()
                .id("cmp-oak-main")
                .tenantId(tenantId)
                .institutionId("inst-oakridge")
                .name("Oakridge International Campus")
                .code("OAK-MAIN")
                .address("120 Academy Way, Austin, TX 78701")
                .contactPhone("+1 (555) 749-1022")
                .contactEmail("austin@oakridge.edu")
                .studentCapacity(1000)
                .gradesOffered("K-12")
                .active(true)
                .build();
        campusStore.put(tenantId, new ArrayList<>(List.of(oakMain)));
    }

    private String resolveTenant(String tenantId) {
        if (tenantId != null && !tenantId.isBlank()) return tenantId;
        String ctxTenant = TenantContext.getTenantId();
        return (ctxTenant != null && !ctxTenant.isBlank()) ? ctxTenant : "apex-high";
    }

    public List<Campus> getCampuses(String tenantId) {
        String tid = resolveTenant(tenantId);
        return campusStore.getOrDefault(tid, Collections.emptyList());
    }

    public Campus createCampus(String tenantId, CreateCampusRequest request) {
        String tid = resolveTenant(tenantId);
        Campus campus = Campus.builder()
                .id("cmp-" + UUID.randomUUID().toString().substring(0, 8))
                .tenantId(tid)
                .institutionId("inst-" + tid)
                .name(request.getName())
                .code(request.getCode())
                .address(request.getAddress())
                .contactPhone(request.getContactPhone())
                .contactEmail(request.getContactEmail())
                .studentCapacity(request.getStudentCapacity())
                .gradesOffered(request.getGradesOffered())
                .active(true)
                .build();

        campusStore.computeIfAbsent(tid, k -> new ArrayList<>()).add(campus);
        return campus;
    }

    public List<AcademicYear> getAcademicYears(String tenantId) {
        String tid = resolveTenant(tenantId);
        return academicYearStore.getOrDefault(tid, Collections.emptyList());
    }

    public Optional<AcademicYear> getCurrentAcademicYear(String tenantId) {
        String tid = resolveTenant(tenantId);
        return academicYearStore.getOrDefault(tid, Collections.emptyList())
                .stream()
                .filter(ay -> "CURRENT".equalsIgnoreCase(ay.getStatus()))
                .findFirst();
    }

    public List<ClassGrade> getClassGrades(String tenantId) {
        String tid = resolveTenant(tenantId);
        return classGradeStore.getOrDefault(tid, Collections.emptyList());
    }

    public List<Subject> getSubjects(String tenantId) {
        String tid = resolveTenant(tenantId);
        return subjectStore.getOrDefault(tid, Collections.emptyList());
    }

    public List<Classroom> getClassrooms(String tenantId) {
        String tid = resolveTenant(tenantId);
        return classroomStore.getOrDefault(tid, Collections.emptyList());
    }

    public InstitutionOverviewDto getOverview(String tenantId) {
        String tid = resolveTenant(tenantId);
        List<Campus> campuses = getCampuses(tid);
        int totalCap = campuses.stream().mapToInt(Campus::getStudentCapacity).sum();

        Optional<AcademicYear> currentYear = getCurrentAcademicYear(tid);
        String yearName = currentYear.map(AcademicYear::getName).orElse("2026–2027");
        String termName = currentYear.flatMap(ay -> ay.getTerms().stream()
                .filter(t -> "CURRENT".equalsIgnoreCase(t.getStatus()))
                .map(AcademicTerm::getTermName)
                .findFirst()).orElse("Fall Term 1");

        List<ClassGrade> classes = getClassGrades(tid);
        int totalSections = classes.stream().mapToInt(c -> c.getSections().size()).sum();
        int totalSubjects = getSubjects(tid).size();
        int totalRooms = getClassrooms(tid).size();

        return InstitutionOverviewDto.builder()
                .tenantId(tid)
                .totalCampuses(campuses.size())
                .totalStudentCapacity(totalCap)
                .currentAcademicYear(yearName)
                .currentTerm(termName)
                .totalClasses(classes.size())
                .totalSections(totalSections)
                .totalSubjects(totalSubjects)
                .totalClassrooms(totalRooms)
                .build();
    }
}
