package com.webgis.service.imp;

import com.webgis.domain.base.BaseDomain;
import com.webgis.domain.cover.CommonCoverDomain;
import com.webgis.enums.TypeEnum;
import com.webgis.mongo.MongoLayerRepository;
import com.webgis.mongo.MongoMapRepository;
import com.webgis.mongo.entity.MongoLayer;
import com.webgis.mongo.entity.MongoMap;
import com.webgis.service.ExcelService;
import com.webgis.service.LayerService;
import com.webgis.web.BaseResult;
import com.webgis.web.dto.WebLayer;
import com.webgis.web.dto.WebLineLayer;
import com.webgis.web.dto.WebPointLayer;
import com.webgis.web.dto.WebMapContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Justin on 2017/3/16.
 *
 * 图层相关接口实现
 *
 */

@Service
public class LayerServiceImp implements LayerService {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private MongoMapRepository mongoMapRepository;

    @Autowired
    private MongoLayerRepository mongoLayerRepository;


    /**
     * 新建图层
     * @param file
     * @param mapId
     * @param type
     * @return
     */
    @Override
    public BaseResult<Object> addLayer(MultipartFile file, int mapId, TypeEnum type) {


        Date currentTime = new Date();

        BaseDomain baseDomain = null;
        switch (type) {
            case YJG:
                baseDomain = excelService.pointExcelAnalysis(file);
                break;
            case XSG:
                baseDomain = excelService.lineExcelAnalysis(file);
                break;
            default:
                break;
        }
        MongoLayer mongoLayer = new MongoLayer(baseDomain, currentTime, currentTime);

        mongoLayer = mongoLayerRepository.save(mongoLayer);
        String layerId = mongoLayer.getId();
        MongoMap mongoMap = mongoMapRepository.findByMapId(mapId);
        mongoMap.getLayerIds().add(layerId);
        mongoMapRepository.save(mongoMap);

        return new BaseResult<>();
    }

    /**
     * 更新点类图层信息
     * @param webLayer
     * @return
     */
    @Override
    public BaseResult<Object> updateLayer(WebLayer webLayer) {
        MongoLayer mongoLayer = mongoLayerRepository.findById(webLayer.getLayerId());
        switch (mongoLayer.getData().getType()) {
            case YJG:
                mongoLayer.setData(((WebPointLayer)webLayer).getData());
                break;
            case XSG:
                mongoLayer.setData(((WebLineLayer)webLayer).getData());
                break;
            default:
                break;
        }
        mongoLayer.setUpdateTime(new Date());
        mongoLayerRepository.save(mongoLayer);

        return new BaseResult<>();
    }

    /**
     * 删除图层
     * @param mapId
     * @param layerId
     * @return
     */
    @Override
    public BaseResult<Object> deleteLayer(int mapId, String layerId) {
        mongoLayerRepository.delete(layerId);
        MongoMap mongoMap = mongoMapRepository.findByMapId(mapId);

        Iterator<String> it = mongoMap.getLayerIds().iterator();
        while (it.hasNext()) {
            String x = it.next();
            if (x.equals(layerId)) {
                it.remove();
            }
        }

        mongoMapRepository.save(mongoMap);

        return new BaseResult<>();
    }


    /**
     * 获取图层内容
     * @param mapId
     * @return
     */
    @Override
    public BaseResult<Object> getLayer(int mapId)
    {
        List<MongoLayer> list = new ArrayList<>();
        list.addAll(mongoMapRepository.findByMapId(mapId).getLayerIds().stream().map(layerId -> mongoLayerRepository.findById(layerId)).collect(Collectors.toList()));
        return new BaseResult<>(new WebMapContent(mapId, list));
    }

    /**
     * 获单个图层
     * @param layerId
     * @return
     */
    @Override
    public BaseResult<Object> getLayer(String layerId) {
        return new BaseResult<>(mongoLayerRepository.findById(layerId));
    }
}