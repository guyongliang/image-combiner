package com.freeway.image.combiner.painter;

import com.freeway.image.combiner.element.CombineElement;
import com.freeway.image.combiner.element.ImageElement;
import com.freeway.image.combiner.utils.ElementUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @Author zhaoqing.chen
 * @Date 2020/8/21
 * @Description 图片绘制器
 */
public class ImagePainter implements IPainter {

    @Override
    public void draw(Graphics2D g, CombineElement element, int canvasWidth) throws Exception {

        //强制转成子类
        ImageElement imageElement = (ImageElement) element;

        //读取元素图
        BufferedImage image = imageElement.getImage();

        //计算宽高
        int width = 0;
        int height = 0;

        switch (imageElement.getZoomMode()) {
            case Origin:
                width = image.getWidth();
                height = image.getHeight();
                break;
            case Width:
                width = imageElement.getWidth();
                height = image.getHeight() * width / image.getWidth();
                break;
            case Height:
                height = imageElement.getHeight();
                width = image.getWidth() * height / image.getHeight();
                break;
            case WidthHeight:
                height = imageElement.getHeight();
                width = imageElement.getWidth();
                break;
        }

        //设置圆角
        if (imageElement.getRoundCorner() != null) {
            image = ElementUtil.makeRoundCorner(image, width, height, imageElement.getRoundCorner());
        }

        //高斯模糊
        if (imageElement.getBlur() != null){
            image = ElementUtil.makeBlur(image,imageElement.getBlur());
        }

        //判断是否设置居中
        if (imageElement.isCenter()) {
            int centerX = (canvasWidth - width) / 2;
            imageElement.setX(centerX);
        }

        //旋转
        if (imageElement.getRotate() != null) {
            g.rotate(Math.toRadians(imageElement.getRotate()), imageElement.getX() + imageElement.getWidth() / 2, imageElement.getY() + imageElement.getHeight() / 2);
        }

        //设置透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageElement.getAlpha()));

        //将元素图绘制到画布
        g.drawImage(image, imageElement.getX(), imageElement.getY(), width, height, null);

        //绘制完后反向旋转，以免影响后续元素
        if (imageElement.getRotate() != null) {
            g.rotate(-Math.toRadians(imageElement.getRotate()), imageElement.getX() + imageElement.getWidth() / 2, imageElement.getY() + imageElement.getHeight() / 2);
        }
    }
}

