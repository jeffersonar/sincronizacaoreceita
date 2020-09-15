package sincronizacaoreceita.component;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sincronizacaoreceita.service.ReceitaService;
import sincronizacaoreceita.vo.ImportFile;

/**
 * 
 * @author Jefferson Rodrigues
 */
@Component
public class ImportCommandLineRunner implements CommandLineRunner {

    private static final char SEPARATOR = ';';

    @Autowired
    private ReceitaService receitaService;

    @Override
    public void run(String... args) throws Exception {
        List<String> file = Arrays.asList(args);
        for (String fileItem : file) {
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(fileItem), "ISO-8859-1"), SEPARATOR);
            CSVWriter writer = new CSVWriter(new FileWriter(fileItem + ".tmp"), SEPARATOR);
            String[] nextRecord;
            int line = 0;
            while ((nextRecord = reader.readNext()) != null) {
                /* Desconsidera o cabeçario do arquivo de leitura e escrever o cabeçario no arquivo temporario */
                if (line++ == 0) {
                    writer.writeNext(ImportFile.header());
                    continue;
                }
                ImportFile fileIm = new ImportFile();
                fileIm.addLine(nextRecord);
                ArrayList<String> list = new ArrayList(Arrays.asList(nextRecord));
                try {
                    /* Validando a conta com a receita federal */
                    Boolean validar = receitaService.atualizarConta(fileIm.getAgencia(),
                            fileIm.getConta(),
                            fileIm.getSaldo(),
                            fileIm.getStatus());
                    list.add(validar ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
                    writer.writeNext(list.toArray(new String[0]));
                } catch (RuntimeException ex) {
                    /* caso ocorra algum erro ira ter retorno da receita como falso */
                    list.add(Boolean.FALSE.toString());
                }
            }
            reader.close();
            writer.close();
            /*delete o arquivo orginal */
            boolean isDelete = new File(fileItem).delete();
            
            /* renomeia o arquivo temporario */
            boolean isSucess = new File(fileItem.concat(".tmp")).renameTo(new File(fileItem));
        }
    }

}
