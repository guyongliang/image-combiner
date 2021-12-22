package com.freeway.image.combiner.painter;

import com.freeway.image.combiner.element.CombineElement;
import com.freeway.image.combiner.element.ImageElement;
import com.freeway.image.combiner.element.RectangleElement;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @Author zhaoqing.chen
 * @Date 2020/8/21
 * @Description 矩形绘制器
 */
public class RectanglePainter implements IPainter {

    @Override
    public void draw(Graphics2D g, CombineElement element, int canvasWidth) {

        //强制转成子类
        RectangleElement rectangleElement = (RectangleElement) element;

        //设置颜色
        g.setColor(rectangleElement.getColor());

        //设置居中
        if (rectangleElement.isCenter()) {
            int centerX = (canvasWidth - rectangleElement.getWidth()) / 2;
            rectangleElement.setX(centerX);
        }

        //设置渐变
        if (rectangleElement.getFromColor() != null) {
            float fromX = 0, fromY = 0, toX = 0, toY = 0;
            switch (rectangleElement.getGradientDirection()) {
                case TopBottom:
                    fromX = rectangleElement.getX() + rectangleElement.getWidth() / 2;
                    fromY = rectangleElement.getY() - rectangleElement.getFromExtend();
                    toX = fromX;
                    toY = rectangleElement.getY() + rectangleElement.getHeight() + rectangleElement.getToExtend();
                    break;
                case LeftRight:
                    fromX = rectangleElement.getX() - rectangleElement.getFromExtend();
                    fromY = rectangleElement.getY() + rectangleElement.getHeight() / 2;
                    toX = rectangleElement.getX() + rectangleElement.getWidth() + rectangleElement.getToExtend();
                    toY = fromY;
                    break;
                case LeftTopRightBottom:
                    fromX = rectangleElement.getX() - (float) Math.sqrt(rectangleElement.getFromExtend());
                    fromY = rectangleElement.getY() - (float) Math.sqrt(rectangleElement.getFromExtend());
                    toX = rectangleElement.getX() + rectangleElement.getWidth() + (float) Math.sqrt(rectangleElement.getToExtend());
                    toY = rectangleElement.getY() + rectangleElement.getHeight() + (float) Math.sqrt(rectangleElement.getToExtend());
                    break;
                case RightTopLeftBottom:
                    fromX = rectangleElement.getX() + rectangleElement.getWidth() + (float) Math.sqrt(rectangleElement.getFromExtend());
                    fromY = rectangleElement.getY() - (float) Math.sqrt(rectangleElement.getFromExtend());
                    toX = rectangleElement.getX() - (float) Math.sqrt(rectangleElement.getToExtend());
                    toY = rectangleElement.getY() + rectangleElement.getHeight() + (float) Math.sqrt(rectangleElement.getToExtend());
                    break;
            }
            g.setPaint(new GradientPaint(fromX, fromY, rectangleElement.getFromColor(), toX, toY, rectangleElement.getToColor()));
        } else {
            g.setPaint(null);
        }

        //设置透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, rectangleElement.getAlpha()));

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRoundRect(rectangleElement.getX(), rectangleElement.getY(), rectangleElement.getWidth(), rectangleElement.getHeight(), rectangleElement.getRoundCorner(), rectangleElement.getRoundCorner());
    }
}

