package com.example.demo.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.entity.Document;
import com.example.demo.service.DocumentService;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

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
            return documentService.saveDocument(personId, recipientName, typefile, documentTitle, file);
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

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long id) throws Exception {
        Resource resource = documentService.loadDocumentAsResource(id);

        // กำหนดประเภทไฟล์ (MIME type) อย่างถูกต้อง
        String contentType = Files.probeContentType(Path.of(resource.getFile().getAbsolutePath()));

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Document> updateDocumentStatus(@PathVariable("id") Long id,
            @RequestParam("status") String status) {
        try {
            Document updatedDocument = documentService.updateDocumentStatus(id, status);
            return ResponseEntity.ok(updatedDocument);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable("id") Long id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting document: " + e.getMessage());
        }
    }
    
}
