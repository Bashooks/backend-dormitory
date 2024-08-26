package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.entity.Document;
import com.example.demo.service.DocumentService;
import java.util.List;
@RestController
@RequestMapping("/documents")
@CrossOrigin(origins = "http://localhost:4200")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public Document uploadDocument(
        @RequestParam("personId") Long personId,
        @RequestParam("recipientName") String recipientName,
        @RequestParam("documentTitle") String documentTitle,
        @RequestParam("typefile") String typefile,
        @RequestParam("file") MultipartFile file) {

        try {
            return documentService.saveDocument(personId, recipientName,typefile, documentTitle, file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/person/{personId}")
    public List<Document> getDocumentsByPersonId(@PathVariable("personId") Long personId) {
        return documentService.getDocumentsByPersonId(personId);
    }

    @GetMapping("/all")
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }
}
