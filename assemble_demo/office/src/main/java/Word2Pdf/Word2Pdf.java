package Word2Pdf;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

import java.io.File;
import java.io.FileInputStream;

public class Word2Pdf {
    public static void main(String[] args) throws Exception{
        long l = System.currentTimeMillis();
        File file = new File(Constant.filePath+"JAVA_蒋晴_15892571519.docx");
        FileInputStream fileInputStream = new FileInputStream(file);
        Document document = new Document(fileInputStream);
        document.save(Constant.resulPath+"JAVA_蒋晴_15892571519.pdf", SaveFormat.PDF);
        System.out.println(System.currentTimeMillis()-l);
    }
}
