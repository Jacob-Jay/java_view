package Word2Pdf;

import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;


public class PPt2Pdf {
    public static void main(String[] args) throws Exception{
        long l = System.currentTimeMillis();

        //文档目录的路径。
//实例化代表演示文件的 Presentation 对象
        Presentation presentation = new Presentation(Constant.filePath+"简约清爽中国风古诗词鉴赏PPT模板.pptx");
        try
        {
            //使用默认选项将演示文稿保存为 PDF
            presentation.save(Constant.resulPath + "简约清爽中国风古诗词鉴赏PPT模板.pdf", SaveFormat.Pdf);
        }
        finally
        {
            if (presentation != null) presentation.dispose();
        }


        System.out.println(System.currentTimeMillis()-l);
    }

}
