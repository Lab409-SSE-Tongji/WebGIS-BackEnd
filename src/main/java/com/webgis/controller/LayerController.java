package com.webgis.controller;

import com.webgis.enums.TypeEnum;
import com.webgis.service.LayerService;
import com.webgis.web.BaseResult;
import com.webgis.web.dto.WebLineLayer;
import com.webgis.web.dto.WebPointLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Justin on 2017/3/16.
 *
 * 图层相关接口
 *
 */

@RestController
@RequestMapping("/layer")
public class LayerController {

    @Autowired
    private LayerService layerService;

    /**
     * 新建图层接口
     * @param file
     * @param mapId
     * @param type
     * @return
     */
    @RequestMapping(value = "/addLayer", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Object> addLayer(@RequestParam("file") MultipartFile file, @RequestParam("mapId") int mapId, @RequestParam("type") String type) {
        return layerService.addLayer(file, mapId, TypeEnum.getEnum(type));
    }

    /**
     * 更新点类图层接口
     * @param webPointLayer
     * @return
     */
    @RequestMapping(value = "/point/updateLayer", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Object> updatePointLayer(@RequestBody WebPointLayer webPointLayer) {
        return layerService.updateLayer(webPointLayer);
    }

    /**
     * 更新线类图层接口
     * @param webLineLayer
     * @return
     */
    @RequestMapping(value = "/line/updateLayer", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Object> updateLineLayer(@RequestBody WebLineLayer webLineLayer) {
        return layerService.updateLayer(webLineLayer);
    }

    /**
     * 删除图层接口
     * @param mapId
     * @param layerId
     * @return
     */
    @RequestMapping(value = "/deleteLayer", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Object> deleteLayer(@RequestParam("mapId") int mapId, @RequestParam("layerId") String layerId) {
        return layerService.deleteLayer(mapId, layerId);
    }

    /**
     * 获取图层数据接口
     * @param mapId
     * @return
     */
    @RequestMapping(value = "/getLayer", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Object> getLayer(@RequestParam("mapId") int mapId) {
        return layerService.getLayer(mapId);
    }
}