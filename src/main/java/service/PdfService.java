package service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.concurrent.Task;
import model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

import static util.ConstantsUtil.*;

/**
 * Service class to get create pdfs.
 *
 * @author Avik Sarkar
 */
public class PdfService {

    /**
     * @param title
     * @return
     */
    private PdfPTable createPdfHeader(Phrase title) {

        FileHandlingService fileHandlingService = new FileHandlingService();

        Map<String, String> propMap = fileHandlingService.loadPropertiesValuesFromPropertiesFile
                ("settings.properties", "universityName", "universityLogoLocation");

        PdfPTable table = new PdfPTable(3);

        try {
            table.setWidths(new float[]{2, 5, 2});

            Image image = Image.getInstance(propMap.get("universityLogoLocation"));
            image.scaleAbsolute(80, 80);

            Phrase phrase = new Phrase();
            phrase.add(new Phrase(propMap.get("universityName") + "\n\n", TITLE_FONT));
            phrase.add(title);

            PdfPCell c = new PdfPCell(image);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);

            c = new PdfPCell(phrase);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(c);

            c = new PdfPCell(Image.getInstance(image));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c);

        } catch (DocumentException | IOException e) {

            e.printStackTrace();
        }
        return table;
    }

    /**
     * @param examRoutine
     * @param examDates
     * @param semestersList
     * @param examDetails
     * @return
     */
    public Task<Boolean> getCreateRoutinePdfTask(List<Exam> examRoutine, List<String> examDates
            , List<String> semestersList, ExamDetails examDetails) {


        final String startTime = LocalTime.parse(examDetails.getStartTime())
                .format(DateTimeFormatter.ofPattern("hh:mm a"));
        final String endTime = LocalTime.parse(examDetails.getEndTime())
                .format(DateTimeFormatter.ofPattern("hh:mm a"));

        Task<Boolean> createRoutinePdfTask = new Task<>() {
            @Override
            protected Boolean call() {

                boolean status = false;

                for (String semester : semestersList) {

                    Path destDirPath = Paths.get(USER_HOME, ROOT_DIR, PDF_DIR);

                    String dest = USER_HOME + FILE_SEPARATOR + ROOT_DIR + FILE_SEPARATOR + PDF_DIR
                            + FILE_SEPARATOR + "routine_semester" + semester + "_" + examDetails.getExamType()
                            .toLowerCase() + "_" + examDetails.getAcademicYear() + ".pdf";

                    Phrase pdfTitle = new Phrase("Routine for Semester " + semester + " "
                            + examDetails.getExamType() + " Examination\n"
                            + examDetails.getAcademicYear(), RED_SUBTITLE_FONT);

                    Document document = new Document();
                    try {

                        Files.createDirectories(destDirPath);
                        PdfWriter.getInstance(document, new FileOutputStream(new File(dest)));
                        document.open();

                        int i = 1;
                        for (String date : examDates) {

                            String formattedDate = LocalDate.parse(date)
                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));


                            PdfPTable table1 = new PdfPTable(3);
                            table1.setWidths(new float[]{2, 1, 4});

                            PdfPTable table2 = new PdfPTable(4);
                            table2.setWidths(new float[]{1, 2, 2, 4});

                            PdfPTable table3 = new PdfPTable(4);
                            table3.setWidths(new float[]{1, 2, 2, 4});


                            PdfPCell c1 = new PdfPCell(new Phrase("DATE-" + formattedDate, BLUE_SUBTITLE_FONT));
                            table1.addCell(c1);

                            c1 = new PdfPCell(new Phrase("DAY-" + i, BLUE_SUBTITLE_FONT));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table1.addCell(c1);

                            c1 = new PdfPCell(new Phrase("TIME-" + startTime + "-" + endTime
                                    , BLUE_SUBTITLE_FONT));
                            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table1.addCell(c1);

                            PdfPCell c2 = new PdfPCell(new Phrase("Sl.", BLUE_SUBTITLE_FONT));
                            table2.addCell(c2);

                            c2 = new PdfPCell(new Phrase("Stream", BLUE_SUBTITLE_FONT));
                            table2.addCell(c2);

                            c2 = new PdfPCell(new Phrase("Subject Code", BLUE_SUBTITLE_FONT));
                            table2.addCell(c2);

                            c2 = new PdfPCell(new Phrase("Subject Name", BLUE_SUBTITLE_FONT));
                            table2.addCell(c2);

                            int j = 1;
                            boolean isSubjectsPresent = false;
                            for (Exam exam : examRoutine) {

                                if (exam.getSemester().equals(semester) && exam.getExamDate().equals(date)) {

                                    PdfPCell c3 = new PdfPCell(new Phrase(String.valueOf(j++), BLUE_SUBTITLE_FONT));
                                    table3.addCell(c3);

                                    c3 = new PdfPCell(new Phrase(exam.getCourseId(), BLUE_SUBTITLE_FONT));
                                    table3.addCell(c3);

                                    c3 = new PdfPCell(new Phrase(exam.getSubId(), CONTENT_FONT1));
                                    table3.addCell(c3);

                                    c3 = new PdfPCell(new Phrase(exam.getSubName(), CONTENT_FONT1));
                                    table3.addCell(c3);

                                    isSubjectsPresent = true;
                                }
                            }

                            if (isSubjectsPresent) {

                                document.add(createPdfHeader(pdfTitle));
                                document.add(new Paragraph(" "));

                                document.add(table1);
                                document.add(new Paragraph(" "));

                                document.add(table2);
                                document.add(new Paragraph(" "));

                                document.add(table3);

                                document.newPage();

                                i++;
                            }
                        }
                        status = true;
                    } catch (IOException | DocumentException e) {

                        e.printStackTrace();
                    } finally {

                        document.close();
                    }
                }
                return status;
            }
        };
        return createRoutinePdfTask;
    }

    /**
     * @param examDetails
     * @return
     */
    public Task<Boolean> getCreateRoomAllocationPdfTask(ExamDetails examDetails) {

        Task<Boolean> createRoomAllocationPdfTask = new Task<>() {

            @Override
            protected Boolean call() {

                BatchService batchService = new BatchService();
                ExamService examService = new ExamService();

                List<Batch> batchList = batchService.getBatchData("");

                boolean status = false;

                Path destDirPath = Paths.get(USER_HOME, ROOT_DIR, PDF_DIR);

                String dest = USER_HOME + FILE_SEPARATOR + ROOT_DIR + FILE_SEPARATOR + PDF_DIR
                        + FILE_SEPARATOR + "room_allocation_" + examDetails.getSemesterType().toLowerCase() + "_" +
                        examDetails.getExamType().toLowerCase() + "_" + examDetails.getAcademicYear() + ".pdf";

                Phrase pdfTitle = new Phrase("Room Allocation for " + examDetails.getExamType()
                        + " Examination\n" + examDetails.getAcademicYear(), RED_SUBTITLE_FONT);

                Document document = new Document();

                try {

                    Files.createDirectories(destDirPath);
                    PdfWriter.getInstance(document, new FileOutputStream(new File(dest)));
                    document.open();

                    int i = 1;

                    PdfPTable table1 = new PdfPTable(5);
                    table1.setWidths(new float[]{1, 1, 2, 1, 3});

                    PdfPTable table2 = new PdfPTable(5);
                    table2.setWidths(new float[]{1, 1, 2, 1, 3});

                    document.add(createPdfHeader(pdfTitle));
                    document.add(new Paragraph(" "));

                    PdfPCell c1 = new PdfPCell(new Phrase("Sl. ", BLUE_SUBTITLE_FONT));
                    c1.setPadding(5);
                    table1.addCell(c1);

                    c1 = new PdfPCell(new Phrase("Degree", BLUE_SUBTITLE_FONT));
                    c1.setPadding(5);
                    table1.addCell(c1);

                    c1 = new PdfPCell(new Phrase("Discipline", BLUE_SUBTITLE_FONT));
                    c1.setPadding(5);
                    table1.addCell(c1);

                    c1 = new PdfPCell(new Phrase("Room No.", BLUE_SUBTITLE_FONT));
                    c1.setPadding(5);
                    table1.addCell(c1);

                    c1 = new PdfPCell(new Phrase("Reg ID.", BLUE_SUBTITLE_FONT));
                    c1.setPadding(5);
                    table1.addCell(c1);

                    document.add(table1);
                    document.add(new Paragraph(" "));

                    for (Batch batch : batchList) {

                        String additionalQuery = "and v_batch_id=? ORDER BY v_reg_id";
                        List<RoomAllocation> roomAllocationList = examService.getRoomAllocation(additionalQuery
                                , examDetails.getExamDetailsId(), batch.getBatchId());

                        for (RoomAllocation roomAllocation : roomAllocationList) {

                            PdfPCell c2 = new PdfPCell(new Phrase(String.valueOf(i++), BLUE_SUBTITLE_FONT));
                            c2.setPadding(5);
                            table2.addCell(c2);

                            c2 = new PdfPCell(new Phrase(batch.getDegree(), BLUE_SUBTITLE_FONT));
                            c2.setPadding(5);
                            table2.addCell(c2);

                            c2 = new PdfPCell(new Phrase(batch.getDiscipline(), BLUE_SUBTITLE_FONT));
                            c2.setPadding(5);
                            table2.addCell(c2);

                            c2 = new PdfPCell(new Phrase(roomAllocation.getRoomNo(), BLUE_SUBTITLE_FONT));
                            c2.setPadding(5);
                            table2.addCell(c2);

                            c2 = new PdfPCell(new Phrase(roomAllocation.getStudentList().get(0).getRegId() + "-" +
                                    roomAllocation.getStudentList().get(roomAllocation.getStudentList().size() - 1).getRegId()
                                    , CONTENT_FONT1));
                            c2.setPadding(5);
                            table2.addCell(c2);
                        }
                    }
                    document.add(table2);

                    status = true;
                } catch (DocumentException | IOException e) {

                    e.printStackTrace();
                } finally {

                    document.close();
                }
                return status;
            }
        };
        return createRoomAllocationPdfTask;
    }

    /**
     * @param roomAllocationList
     * @param examDetails
     * @return
     */
    @SuppressWarnings("Duplicates")
    public Task<Boolean> getCreateSeatArrangementPdfTask(List<RoomAllocation> roomAllocationList
            , ExamDetails examDetails) {

        Task<Boolean> createSeatArrangementPdfTask = new Task<>() {

            @Override
            protected Boolean call() {

                boolean status = false;

                Path destDirPath = Paths.get(USER_HOME, ROOT_DIR, PDF_DIR);

                String dest = USER_HOME + FILE_SEPARATOR + ROOT_DIR + FILE_SEPARATOR + PDF_DIR
                        + FILE_SEPARATOR + "seat_arrangement_" + examDetails.getSemesterType().toLowerCase() + "_" +
                        examDetails.getExamType().toLowerCase() + "_" + examDetails.getAcademicYear() + ".pdf";

                Phrase pdfTitle = new Phrase("Seat Arrangement for " + examDetails.getExamType()
                        + " Examination\n" + examDetails.getAcademicYear(), RED_SUBTITLE_FONT);

                Document document = new Document();

                try {

                    Files.createDirectories(destDirPath);
                    PdfWriter.getInstance(document, new FileOutputStream(new File(dest)));
                    document.open();

                    for (RoomAllocation roomAllocation : roomAllocationList) {

                        int rows = Integer.parseInt(roomAllocation.getNoOfRows());
                        int cols = Integer.parseInt(roomAllocation.getNoOfCols());

                        List<Student> studentListInRoom = roomAllocation.getStudentList();
                        Map<Integer, Integer> roomAllocationIdMap = roomAllocation.getRoomAllocationMap();

                        PdfPTable mainTable = new PdfPTable(cols);

                        PdfPTable tableTitle = new PdfPTable(1);
                        PdfPCell titleCell = new PdfPCell(new Phrase("Room No. " + roomAllocation.getRoomNo(),
                                HEADING_FONT));
                        titleCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        titleCell.setBorder(PdfPCell.NO_BORDER);
                        tableTitle.addCell(titleCell);

                        PdfPTable instruction = new PdfPTable(1);

                        Image image = Image.getInstance(getClass().getResource("/png/chalkboard.png"));
                        PdfPCell c = new PdfPCell(image);
                        c.setBorder(PdfPCell.NO_BORDER);
                        c.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        instruction.addCell(c);

                        c = new PdfPCell(new Phrase("All eyes this way please", CONTENT_FONT2));
                        c.setBorder(PdfPCell.NO_BORDER);
                        c.setPadding(10);
                        c.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        c.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                        instruction.addCell(c);

                        for (int i = 0; i < rows; i++) {

                            for (int j = 0; j < cols; j++) {

                                PdfPCell outerCell = new PdfPCell();
                                outerCell.setBorder(PdfPCell.NO_BORDER);
                                outerCell.setPadding(5);

                                PdfPTable pdfPTable = new PdfPTable(1);
                                pdfPTable.setWidths(new float[]{1});

                                PdfPCell pdfPCell;

                                int currStudentIndex = j * rows + i;
                                if (roomAllocationIdMap.containsKey(currStudentIndex)) {

                                    Student currStudent = studentListInRoom.get(
                                            roomAllocationIdMap.get(currStudentIndex));

                                    pdfPCell = new PdfPCell(new Phrase(currStudent.getRegId(), CONTENT_FONT1));
                                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                } else {

                                    pdfPCell = new PdfPCell(new Phrase(" "));
                                }
                                pdfPTable.addCell(pdfPCell);

                                outerCell.addElement(pdfPTable);

                                mainTable.addCell(outerCell);
                            }
                        }
                        document.add(createPdfHeader(pdfTitle));
                        document.add(new Paragraph(" "));

                        document.add(tableTitle);
                        document.add(new Paragraph(" "));

                        document.add(new Paragraph(" "));

                        document.add(instruction);
                        document.add(new Paragraph(" "));

                        document.add(mainTable);
                        document.add(new Paragraph(" "));

                        document.newPage();
                    }
                    status = true;
                } catch (DocumentException | IOException e) {

                    e.printStackTrace();
                } finally {

                    document.close();
                }
                return status;
            }
        };
        return createSeatArrangementPdfTask;
    }

    /**
     * This method is used to get a task which can be used to create the invigilation duty PDF.
     *
     * @param invigilationDutyList The list of invigilation duties from which the PDF will be created.
     * @param examDetails          The details of the exam , the invigilation duties belong to.
     * @return A task which can be used to create invigilation duty pdf in a separate thread.
     */
    public Task<Boolean> getCreateInvigilationDutyPdfTask(List<InvigilationDuty> invigilationDutyList
            , ExamDetails examDetails) {

        Task<Boolean> createInvigilationDutyPdfTask = new Task<>() {
            @Override
            protected Boolean call() {

                //initially pdf creation status is negative
                boolean status = false;

                //tree map to keep the order in which data is inserted into the map
                Map<String, Map<String, List<String>>> map = new TreeMap<>();

                for (InvigilationDuty invigilationDuty : invigilationDutyList) {

                    map.putIfAbsent(invigilationDuty.getExamDate(), new HashMap<>());

                    map.get(invigilationDuty.getExamDate()).putIfAbsent(invigilationDuty.getRoomNo(), new ArrayList<>());

                    map.get(invigilationDuty.getExamDate()).get(invigilationDuty.getRoomNo()).add(invigilationDuty.getProfId());
                }

                Path destDirPath = Paths.get(USER_HOME, ROOT_DIR, PDF_DIR);

                String dest = USER_HOME + FILE_SEPARATOR + ROOT_DIR + FILE_SEPARATOR + PDF_DIR
                        + FILE_SEPARATOR + "invigilation_duty_" + examDetails.getSemesterType().toLowerCase() + "_" +
                        examDetails.getExamType().toLowerCase() + "_" + examDetails.getAcademicYear() + ".pdf";

                Phrase pdfTitle = new Phrase("Invigilation Duty Chart for " + examDetails.getExamType()
                        + " Examination\n" + examDetails.getAcademicYear(), RED_SUBTITLE_FONT);

                Document document = new Document();

                try {

                    Files.createDirectories(destDirPath);
                    PdfWriter.getInstance(document, new FileOutputStream(new File(dest)));
                    document.open();

                    final String startTime = LocalTime.parse(examDetails.getStartTime())
                            .format(DateTimeFormatter.ofPattern("hh:mm a"));
                    final String endTime = LocalTime.parse(examDetails.getEndTime())
                            .format(DateTimeFormatter.ofPattern("hh:mm a"));

                    int i = 1;

                    for (Map.Entry<String, Map<String, List<String>>> entry : map.entrySet()) {

                        String date = entry.getKey();
                        String formattedDate = LocalDate.parse(date)
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                        int j = 1;

                        PdfPTable table1 = new PdfPTable(3);
                        table1.setWidths(new float[]{2, 1, 4});

                        PdfPCell c1 = new PdfPCell(new Phrase("DATE - " + formattedDate, BLUE_SUBTITLE_FONT));
                        table1.addCell(c1);

                        c1 = new PdfPCell(new Phrase("DAY - " + i++, BLUE_SUBTITLE_FONT));
                        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(c1);

                        c1 = new PdfPCell(new Phrase("TIME - " + startTime + "-" + endTime
                                , BLUE_SUBTITLE_FONT));
                        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(c1);

                        PdfPTable table2 = new PdfPTable(3);
                        table2.setWidths(new float[]{1, 3, 3});

                        PdfPCell c2 = new PdfPCell(new Phrase("Sl.", BLUE_SUBTITLE_FONT));
                        table2.addCell(c2);

                        c2 = new PdfPCell(new Phrase("Room No.", BLUE_SUBTITLE_FONT));
                        table2.addCell(c2);

                        c2 = new PdfPCell(new Phrase("Professor ID.", BLUE_SUBTITLE_FONT));
                        table2.addCell(c2);

                        PdfPTable table3 = new PdfPTable(3);
                        table3.setWidths(new float[]{1, 3, 3});

                        document.add(createPdfHeader(pdfTitle));
                        document.add(new Paragraph(" "));

                        document.add(table1);
                        document.add(new Paragraph(" "));

                        document.add(table2);
                        document.add(new Paragraph(" "));

                        for (Map.Entry<String, List<String>> entry1 : entry.getValue().entrySet()) {

                            for (String profId : entry1.getValue()) {

                                PdfPCell c3 = new PdfPCell(new Phrase(String.valueOf(j++), BLUE_SUBTITLE_FONT));
                                c3.setHorizontalAlignment(Element.ALIGN_CENTER);
                                c3.setPadding(5);
                                table3.addCell(c3);

                                c3 = new PdfPCell(new Phrase(entry1.getKey(), BLUE_SUBTITLE_FONT));
                                c3.setPadding(5);
                                table3.addCell(c3);

                                c3 = new PdfPCell(new Phrase(profId, CONTENT_FONT1));
                                c3.setPadding(5);
                                table3.addCell(c3);
                            }
                        }
                        document.add(table3);
                        document.newPage();
                    }
                    status = true;

                } catch (DocumentException | IOException e) {

                    e.printStackTrace();
                } finally {

                    document.close();
                }
                return status;
            }
        };
        return createInvigilationDutyPdfTask;
    }
}
