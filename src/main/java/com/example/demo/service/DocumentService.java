package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.entity.Document;
import com.example.demo.entity.Person;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.PersonRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.UUID;
import java.util.List;
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final PersonRepository personRepository;
    private final Path rootLocation = Paths.get("upload-dir");

    public DocumentService(DocumentRepository documentRepository, PersonRepository personRepository) {
        this.documentRepository = documentRepository;
        this.personRepository = personRepository;

        // ตรวจสอบและสร้างไดเรกทอรีถ้าไม่พบ
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public Document saveDocument(Long personId, String recipientName,String typefile,String documentTitle, MultipartFile file ) throws Exception {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new Exception("Person not found"));

        String fileName = file.getOriginalFilename();
        Path destinationFile = this.rootLocation.resolve(Paths.get(fileName))
                .normalize().toAbsolutePath();

        // ตรวจสอบว่ามีไฟล์ชื่อเดียวกันอยู่หรือไม่ ถ้ามีให้เปลี่ยนชื่อ
        if (Files.exists(destinationFile)) {
            String newFileName = UUID.randomUUID().toString() + "_" + fileName;
            destinationFile = this.rootLocation.resolve(Paths.get(newFileName))
                    .normalize().toAbsolutePath();
            fileName = newFileName;
        }

        Files.copy(file.getInputStream(), destinationFile);

        Document document = new Document();
        document.setSenderName(person.getFirstName() + " " + person.getLastName());
        document.setRecipientName(recipientName);
        document.setDocumentTitle(documentTitle);
        document.setFileName(fileName);
        document.setTypefile(typefile); 
        document.setFilePath(destinationFile.toString());
        document.setPerson(person);
        document.setStatus("รอตรวจสอบ");
        return documentRepository.save(document);
    }

    public List<Document> getDocumentsByPersonId(Long personId) {
        return documentRepository.findByPersonId(personId);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
}
