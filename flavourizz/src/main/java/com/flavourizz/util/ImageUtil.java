package com.flavourizz.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class ImageUtil {

	public static String saveImage(Part filePart, HttpServletRequest request, String subfolder) throws IOException {
	    String fileName = System.currentTimeMillis() + "_" +
	            Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

	    String uploadPath = request.getServletContext().getRealPath("/resources/images/" + subfolder);

	    File uploadDir = new File(uploadPath);
	    if (!uploadDir.exists()) {
	        uploadDir.mkdirs();
	    }

	    String fullSavePath = uploadPath + File.separator + fileName;
	    filePart.write(fullSavePath);  // Saves image to deployed folder

	    System.out.println("Image saved to: " + fullSavePath);
	    return fileName;
	}
}
