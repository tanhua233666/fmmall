package com.tanhua.fmmall.dao;

import com.tanhua.fmmall.entity.IndexImg;
import com.tanhua.fmmall.general.GeneralDao;

import java.util.List;

public interface IndexImgMapper extends GeneralDao<IndexImg> {

    //1.查询轮播图信息：查询status=1，且按seq进行排序
    public List<IndexImg> listIndexImgs();
}