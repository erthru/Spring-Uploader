package com.ertohru.springuploader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class ImageController {

	@Autowired
	private ImageRepository imageRepo;
	
	@GetMapping("/")
	public List<Image> all(){
		return imageRepo.findAll();
	}
	
	@PostMapping("/")
	public Image add(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
		
		String uniqid = UUID.randomUUID().toString();
		
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get("./src/main/resources/static/uploads/" + uniqid + ".jpg");
			Files.write(path, bytes);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Image image = new Image();
		image.setName(name);
		image.setFile(uniqid+".jpg");
		
		return imageRepo.save(image);
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") long id) {
		
		Optional<Image> image = imageRepo.findById(id);
		
		try {
			imageRepo.delete(image.get());
			Path path = Paths.get("./src/main/resources/static/uploads/"+image.get().getFile());
			Files.delete(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "file deleted";
	}
	
}
