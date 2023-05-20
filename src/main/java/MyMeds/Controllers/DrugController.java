package MyMeds.Controllers;

import MyMeds.App.Drug;
import MyMeds.Services.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/drug")
@CrossOrigin
public class DrugController {

    @Autowired
    private DrugService drugService;

    @GetMapping
    private List<DrugService.DrugDto> getDrugs(){
        List<String> filePaths=new ArrayList<>();
        filePaths.add("src/main/resources/drugs/medicine1.csv");
        filePaths.add("src/main/resources/drugs/medicine2.csv");
        return drugService.getAllDrugsFromCSV(filePaths);
    }
}
