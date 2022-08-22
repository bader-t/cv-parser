package com.example.ocrtest.services;

import com.example.ocrtest.DTOs.ContentResponseDTO;
import com.example.ocrtest.entities.*;
import com.example.ocrtest.helpers.DateParser;
import com.example.ocrtest.services.sectionImpl.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CVParserServiceImpl implements CVParserService{

    public String fileName = null;
    private final Map<SectionType, Pattern> sectionMap = new HashMap<>();
    private final static DateParser dateParser = new DateParser();
    private final static String dateRegex = "[a-z]{3}\\s\\d{4}|[a-z]{4,}\\s\\d{4}|\\d{1,2}-\\d{1,2}-\\d{4}|\\d{1,2}\\s\\d{1,2}\\s\\d{4}|\\d{4}-\\d{1,2}-\\d{1,2}|\\d{1,2}/\\d{1,2}/\\d{4}|\\d{4}/\\d{1,2}/\\d{1,2}|\\d{1,2}\\s[a-z]{3}\\s\\d{4}|\\d{1,2}\\s[a-z]{4,}\\s\\d{4}";

    public ContentResponseDTO parseException(final String content, CV cv){
        String hardskilldata = "JAVA";
        String softskilldata = "";
        String certificationdata = "";
        List<Skill> listofskills = new ArrayList<>();
        List<Certification> listofcertification = new ArrayList<>();
        try {
            ClassLoader classLoader = CVParserServiceImpl.class.getClassLoader();
            File file = new File(classLoader.getResource("skillsdataset.txt").getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));
            hardskilldata = br.readLine();
            br.close();
            file = new File(classLoader.getResource("soft_skills.txt").getFile());
            br = new BufferedReader(new FileReader(file));
            softskilldata = br.readLine();
            br.close();
            file = new File(classLoader.getResource("certifications.txt").getFile());
            br = new BufferedReader(new FileReader(file));
            certificationdata = br.readLine();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Pattern skillspattern = Pattern.compile(hardskilldata,Pattern.CASE_INSENSITIVE);
        Pattern softskillspattern = Pattern.compile(softskilldata,Pattern.CASE_INSENSITIVE);
        Pattern certificationpattern = Pattern.compile(certificationdata,Pattern.CASE_INSENSITIVE);
        ContentResponseDTO response = new ContentResponseDTO();
        response.setContent(content);
        String emailregex = "[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        String phoneregex = "(\\+212|0)([ \\-_/]*)(\\d[ \\-_/]*){9}";
        Pattern emailpattern = Pattern.compile(emailregex);
        Pattern phonepattern = Pattern.compile(phoneregex);
        Pattern datepattern = Pattern.compile(dateRegex,Pattern.CASE_INSENSITIVE);
        String dateoftoday = "not specified";
        if(cv.getPhoneNumber()==null || cv.getEmail()==null || cv.getSkills()==null){
            int i=0;
            for(String line:content.split("\n")){
                Matcher matcheremail = emailpattern.matcher(line);
                Matcher matcherphone = phonepattern.matcher(line);
                Matcher matcherskills = skillspattern.matcher(line);
                Matcher matchersoftskills = softskillspattern.matcher(line);
                Matcher matchercertification = certificationpattern.matcher(line);
                Matcher matcherdate = datepattern.matcher(line);
                if(matcherphone.find()){
                    cv.setPhoneNumber(matcherphone.group());
                }
                if(matcheremail.find()){
                    cv.setEmail(matcheremail.group());
                }
                if(matcherskills.find()){
                    Skill newskill = null;
                    do {
                        newskill = new Skill(matcherskills.group(), "Programming");
                        listofskills.add(newskill);
                    }while (matcherskills.find());
                }
                if(matchersoftskills.find()){
                    Skill newsoftskill = null;
                    do{
                        newsoftskill = new Skill(matchersoftskills.group(),"Soft Skill");
                        listofskills.add(newsoftskill);
                    }while (matchersoftskills.find());
                }
                if(matchercertification.find()){
                    Certification newsoftskill = null;
                    if(matcherdate.find()){
                        dateoftoday = dateParser.parseDate(matcherdate.group());
                    }
                    do{
                        newsoftskill = new Certification(matchercertification.group(),dateoftoday);
                        listofcertification.add(newsoftskill);
                    }while (matchercertification.find());
                }

            }
            List<Skill> cleanlist = listofskills.stream().distinct().collect(Collectors.toList());
            List<Certification> cleancertificatiolist = listofcertification.stream().distinct().collect(Collectors.toList());
            cv.setSkills(cleanlist);
            cv.setCertifications(cleancertificatiolist);
            response.setCv(cv);
        }
        return response;
    }

    public CVParserServiceImpl() {
        this.sectionMap.put(SectionType.Skills,Pattern.compile("^Skills$|Compétence|expertise|Talents",Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CHARACTER_CLASS));
        this.sectionMap.put(SectionType.Education,Pattern.compile("Education|Schooling|Learning|Parcours Scolaire",Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CHARACTER_CLASS));
        this.sectionMap.put(SectionType.Interest,Pattern.compile("Interest|centres d'intérêt",Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CHARACTER_CLASS));
        this.sectionMap.put(SectionType.Certification,Pattern.compile("Certification|Certificats|Certif",Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CHARACTER_CLASS));
        this.sectionMap.put(SectionType.Experience,Pattern.compile("Experience|Expérience",Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CHARACTER_CLASS));
        this.sectionMap.put(SectionType.Soft_Skills,Pattern.compile("Soft Skills",Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CHARACTER_CLASS));
    }
    @Override
    public ContentResponseDTO parse(final MultipartFile multipartFile) throws Exception{
        CV cv = new CV();
        ContentResponseDTO response = new ContentResponseDTO();
        Map<SectionType, SectionParser> parserMap = new HashMap<>();

        parserMap.put(SectionType.Personal, new PersonalParser());
        parserMap.put(SectionType.Certification, new CertificationParser());
        parserMap.put(SectionType.Skills,new SkillParser());
        parserMap.put(SectionType.Experience,new ExperienceParser());
        parserMap.put(SectionType.Education, new EducationParser());
        parserMap.put(SectionType.Soft_Skills,new SoftSkillsParser());

        response.setContent(this.extractContent(multipartFile));
        if (this.fileName != null){
            if(this.fileName.contains("_")){
                String[] fullName = this.fileName.split("_", 2);
                cv.setFirstName(fullName[0]);
                cv.setLastName(fullName[1]);
            }else {
                cv.setFirstName(this.fileName);
                cv.setLastName(this.fileName);
            }
        }
        response.setCv(cv);

        String[] lines = response.getContent().split("\n");
        List<Section> sections = this.extractSection(lines);
        for (Section section: sections) {
            System.out.println(section.getType());
            SectionParser sectionParser = parserMap.get(section.getType());
            if (sectionParser != null) {
                sectionParser.parse(section, cv);
            }
            System.out.println(section.getType() + "\n");
            for (String d: section.getContent()
                 ) {
                System.out.println( "+" + d );
            }
            System.out.println("-------------");

        }

        return response;
    }



    public String extractContent(final MultipartFile multipartFile){
        String text;
        this.fileName = Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.",2)[0];

        if(Objects.equals(multipartFile.getContentType(), "application/pdf")) {
            try (final PDDocument document = PDDocument.load(multipartFile.getInputStream())) {


                final PDFTextStripper pdfStripper = new PDFTextStripper();
                pdfStripper.setSortByPosition(true);
                text = pdfStripper.getText(document);

            } catch (final Exception ex) {
                text = "Error parsing PDF";
            }
            return text;

        } else if (Objects.equals(multipartFile.getContentType(), "application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            try (XWPFDocument doc = new XWPFDocument(multipartFile.getInputStream())) {
                XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(doc);
                text = xwpfWordExtractor.getText();
            } catch (Exception e) {
                text = "Error parsing this file";
            }
            return text;

        } return null;
    }


    public List<Section> extractSection(String[] lines){
        List<Section> sections = new ArrayList<>();

        int lineIndex = 0;

        // assume first section contains personal infos
        Section personalSection = new Section(SectionType.Personal);
        while(lineIndex < lines.length && this.findSectionType(lines[lineIndex]) == SectionType.Unknown){
            // delete blank lines
            if(!lines[lineIndex].trim().isEmpty()){
                personalSection.getContent().add(lines[lineIndex]);
            }
            lineIndex++;
        }
        sections.add(personalSection);

        // extract rest of the sections
        while(lineIndex < lines.length){
            SectionType sectionType = this.findSectionType(lines[lineIndex]);
            // new section
            if (sectionType != SectionType.Unknown) {
                Section section = new Section(sectionType);
                // assume the next line is content of current section
                while (lineIndex < lines.length - 1 && this.findSectionType(lines[lineIndex + 1]) == SectionType.Unknown) {
                    lineIndex++;
                    if (!lines[lineIndex].trim().isEmpty()) {
                        section.getContent().add(lines[lineIndex]);
                    }
                }
                sections.add(section);
            }
            lineIndex++;
        }
        return sections;
    }


    public SectionType findSectionType(String line){
        if (line.trim().isEmpty()) {
            return SectionType.Unknown;
        }
        for (Map.Entry<SectionType, Pattern> entry : this.sectionMap.entrySet()) {
            if (entry.getValue().matcher(line).find()) {
                return entry.getKey();
            }
        }
        return SectionType.Unknown;

    }
}
