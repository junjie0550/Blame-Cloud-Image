package com.junjie.blamepicturebackend.controller;

import com.junjie.blamepicturebackend.annotation.AuthCheck;
import com.junjie.blamepicturebackend.common.BaseResponse;
import com.junjie.blamepicturebackend.common.ResultUtils;
import com.junjie.blamepicturebackend.constant.UserConstant;
import com.junjie.blamepicturebackend.model.dto.picture.PictureUploadRequest;
import com.junjie.blamepicturebackend.model.entity.User;
import com.junjie.blamepicturebackend.model.vo.PictureVO;
import com.junjie.blamepicturebackend.service.PictureService;
import com.junjie.blamepicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private UserService userService;

    @Resource
    private PictureService pictureService;

    /**
     * 上传图片（可重新上传）
     */
    @PostMapping("/upload")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<PictureVO> uploadPicture(
            @RequestPart("file") MultipartFile multipartFile,
            PictureUploadRequest pictureUploadRequest,
            HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        PictureVO pictureVO = pictureService.uploadPicture(multipartFile, pictureUploadRequest, loginUser);
        return ResultUtils.success(pictureVO);
    }


}







