package com.cobong.yuja.service.user;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.model.ProfilePicture;
import com.cobong.yuja.model.Thumbnail;
import com.cobong.yuja.payload.request.user.ProfilePictureDto;
import com.cobong.yuja.repository.user.ProfilePictureRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfilePictureServiceImpl implements ProfilePictureService {
	private final ProfilePictureRepository profilePictureRepository;
	
	@Override
	public ProfilePictureDto saveFile(MultipartFile file) {
		ProfilePictureDto dto = new ProfilePictureDto();
		try {
			String origFilename = file.getOriginalFilename();
			String fileType = origFilename.substring(origFilename.lastIndexOf("."));
			String dateNow = new SimpleDateFormat("YYYYMMddHHmmssSSSSSS").format(new Date());
			String filename = dateNow + fileType;
			
			// 실행되는 위치의 'temp' 폴더에 파일이 저장
			String savePath = System.getProperty("user.dir") + File.separator+"files" + File.separator +"temp";
			
			// 파일이 저장되는 폴더가 없으면 폴더를 생성
			if (!new File(savePath).exists()) {
				try {
					new File(savePath).mkdirs(); //mkdirs는 폴더안에 폴더를 찾는데 그 상위폴더 조차 존재치 않으면 만들어준다.
					System.out.println(savePath);
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			savePath += File.separator + filename;
			System.out.println("============>   "+ImageIO.read(file.getInputStream()));
			
			BufferedImage resizedProfile = new BufferedImage(160, 160, ImageIO.read(file.getInputStream()).getType());
	        
			Graphics2D graphics2D = resizedProfile.createGraphics();
	        graphics2D.drawImage(ImageIO.read(file.getInputStream()), 0, 0, 160, 160, null);
	        graphics2D.dispose();
			
	        if(ImageIO.write(resizedProfile, "jpeg", new File(savePath))) {
	        	System.out.println("Hurray!!!");
	        }

	        String uploadPath = System.getProperty("user.dir") + File.separator+"files" + File.separator + "profiles" + File.separator;
			
			if (!new File(uploadPath).exists()) {
				try {
					new File(uploadPath).mkdirs();
					System.out.println(uploadPath);
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			uploadPath += File.separator + filename;
			
			
			dto.setOrigFilename(origFilename);
			dto.setUploadPath(uploadPath);
			dto.setFileName(filename);
			dto.setTempPath(savePath);
			
			ProfilePicture profilePicture = profilePictureRepository.save(dto.toEntitiy());
			dto.setProfilePicId(profilePicture.getProfilePicId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
}
