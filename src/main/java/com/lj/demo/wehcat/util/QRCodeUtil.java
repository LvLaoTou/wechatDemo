package com.lj.demo.wehcat.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 * @author lv
 * @version 1.0.0
 */
@Slf4j
public class QRCodeUtil {

    /**
     * 工具类私有无惨构造
     */
    private QRCodeUtil(){

    }

    public static BufferedImage getQRCodeBufferedImage(String content) throws Exception {
        int width = 300;
        /*
        * 构造二维字节矩阵，将二位字节矩阵渲染为二维缓存图片
        * 谷歌默认编码使用的是ISO-8859-1，为了避免中文乱码所以需要转换编码
        * 参考：com.google.zxing.qrcode.encoder.Encoder类下的static final String DEFAULT_BYTE_MODE_ENCODING = "ISO-8859-1";
        */
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, width,hints);
        BufferedImage image = toBufferedImage(bitMatrix);
        return image;
    }

    /**
     * 生成二维码 生成二维码图片字节流
     * @param content 二维码内容
     * @param width   二维码宽度和高度
     * @param picFormat  二维码图片格式
     */
    public static byte[] generateQRcodeByte(String content, int width, String picFormat) {
        byte[] codeBytes = null;
        try {
            // 构造二维字节矩阵，将二位字节矩阵渲染为二维缓存图片
            BitMatrix bitMatrix = new MultiFormatWriter().encode(new String(content.getBytes("UTF-8"),"ISO-8859-1"), BarcodeFormat.QR_CODE, width, width);
            BufferedImage image = toBufferedImage(bitMatrix);

            // 定义输出流，将二维缓存图片写到指定输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, picFormat, out);

            // 将输出流转换为字节数组
            codeBytes = out.toByteArray();

        } catch (Exception e) {
            log.error("生成二维码异常：{}",e);
        }
        return codeBytes;
    }

    /**
     * 将二维字节矩阵渲染为二维缓存图片
     * @param matrix 二维字节矩阵
     * @return 二维缓存图片
     */
    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int onColor = 0xFF000000;
        int offColor = 0xFFFFFFFF;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
            }
        }
        return image;
    }
}
