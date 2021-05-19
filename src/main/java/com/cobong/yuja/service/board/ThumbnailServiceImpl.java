package com.cobong.yuja.service.board;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cobong.yuja.model.Thumbnail;
import com.cobong.yuja.payload.request.board.ThumbnailDto;
import com.cobong.yuja.repository.attach.ThumbnailRepository;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@RequiredArgsConstructor
public class ThumbnailServiceImpl implements ThumbnailService{
	private final ThumbnailRepository thumbnailRepository;

	private final List<String> availableTypes = Arrays.asList(".jpg",".jpeg",".png",".gif");
	
	@Override
	@Transactional
	public Object saveFile(MultipartFile file) {
		ThumbnailDto dto = new ThumbnailDto();
		try {
			String origFilename = file.getOriginalFilename();
			String fileType = origFilename.substring(origFilename.lastIndexOf("."));
			String dateNow = new SimpleDateFormat("YYYYMMddHHmmssSSSSSS").format(new Date());
			String filename = dateNow + fileType;
			
			if(!availableTypes.contains(fileType)) {
				/***
				 * 파일 형식이 ".jpg",".jpeg",".png",".gif" 중 하나가 아닐시 예외처리 필요
				 */
				throw new IllegalAccessError("파일형식은 jpg, jpeg, png, gif 중 하나여야 합니다!");
			};
			
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
			String originalImg = savePath + File.separator +"original"+ filename;
			
			savePath += File.separator + filename;
			
			FileOutputStream res = new FileOutputStream(new File(savePath));
			if(ImageIO.read(file.getInputStream()).getHeight() >= 202 || ImageIO.read(file.getInputStream()).getWidth() >= 360) {
				if(ImageIO.read(file.getInputStream()).getHeight() < ImageIO.read(file.getInputStream()).getWidth()) {
					Thumbnailator.createThumbnail(file.getInputStream(), res, 360 ,ImageIO.read(file.getInputStream()).getHeight());
				} else {
					Thumbnailator.createThumbnail(file.getInputStream(), res, ImageIO.read(file.getInputStream()).getWidth(), 202);	
				}
			} else {
				Thumbnailator.createThumbnail(file.getInputStream(), res, ImageIO.read(file.getInputStream()).getWidth(), ImageIO.read(file.getInputStream()).getHeight());
			}
			res.close();
			
			file.transferTo(new File(originalImg));

	        String uploadPath = System.getProperty("user.dir") + File.separator+"files" + File.separator + "thumbnail";
			
			if (!new File(uploadPath).exists()) {
				try {
					new File(uploadPath).mkdirs();
					System.out.println(uploadPath);
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			
			String originalUplaodPath = uploadPath + File.separator +"original";
			
			if (!new File(originalUplaodPath).exists()) {
				try {
					new File(originalUplaodPath).mkdirs();
					System.out.println(originalUplaodPath);
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			
			uploadPath += File.separator + filename;
			originalUplaodPath += File.separator +"original"+ filename;
			
			dto.setOrigFilename(origFilename);
			dto.setUploadPath(uploadPath);
			dto.setFileName(filename);
			dto.setTempPath(savePath);
			dto.setOriginalFileTemp(originalImg);
			dto.setOriginalFileDest(originalUplaodPath);
			
			Thumbnail thumbnail = thumbnailRepository.save(dto.toEntitiy());
			dto.setThumbnailId(thumbnail.getThumbnailId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalAccessError("이미지 업로드 중 오류가 발생했습니다");
		}
		return dto;
	}

	@Override
	public void deleteUnflagged() {
		List<Thumbnail> thumbsToDel = thumbnailRepository.findAllByFlag();
		
		for(Thumbnail thumbnail: thumbsToDel) {
			thumbnailRepository.delete(thumbnail);
		}
	}
}
