package com.pcc.wellfare.controllers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pcc.wellfare.service.DeptService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/dept")
@RequiredArgsConstructor
public class DeptController {
	
	private final DeptService deptService;
	
	 @PostMapping(value = "/uploadDepts", consumes = {"multipart/form-data"})
	    public ResponseEntity<Integer> uploadDepts(
	            @RequestPart ("file")MultipartFile file
	            ) throws IOException {
	        return ResponseEntity.ok(deptService.uploadDepts(file));
	    }
}
