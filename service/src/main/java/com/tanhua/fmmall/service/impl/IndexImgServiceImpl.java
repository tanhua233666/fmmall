package com.tanhua.fmmall.service.impl;

import com.tanhua.fmmall.dao.IndexImgMapper;
import com.tanhua.fmmall.entity.IndexImg;
import com.tanhua.fmmall.service.IndexImgService;
import com.tanhua.fmmall.vo.ResultCode;
import com.tanhua.fmmall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndexImgServiceImpl implements IndexImgService {
    @Resource
    private IndexImgMapper indexImgMapper;

    @Override
    public ResultVO listIndexImgs() {
        List<IndexImg> indexImgs = indexImgMapper.listIndexImgs();
        if (indexImgs.size() == 0){
            return new ResultVO(ResultCode.FAILURE,"fail",null);
        } else {
            return new ResultVO(ResultCode.SUCCESS,"success",indexImgs);
        }

    }
}
