package MyMeds.Services;

import MyMeds.App.Drug;
import MyMeds.App.Patient;
import MyMeds.Repositorys.DrugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DrugService {


    @Autowired
    RecipeService recipeService;
    @Autowired//Instancia Spring el servicio.
    UserService userService;
    @Autowired
    DrugRepository drugRepository;
    public record DrugDto(String drugName,String drugDose){}
    public List<DrugDto> getAllDrugsFromCSV(List<String> filePaths){
        List<DrugDto> drugsDtoList = new ArrayList<>();

        for (String filePath : filePaths) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                boolean firstLine = true;

                while ((line = br.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false;
                        continue; // Ignorar la primera línea (encabezados)
                    }

                    String[] data = line.split(",");
                    if (data.length >= 7) {
                        String drugName = data[3].replace("\"", "");
                        String drugDose = data[6].replace("\"", "");

                        DrugDto drugDto = new DrugDto(drugName, drugDose);
                        drugsDtoList.add(drugDto);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return drugsDtoList;
    }

    //Este metodo de momento no funciona correctamente, porque la receta es nula.
    public boolean addDrugToPatient(Integer patientID, Integer doctorID,DrugDto drugDto){
        List<Patient> patients=userService.doctorRepository.findByPatients(doctorID);
        for (Patient p: patients) {
            if (Objects.equals(p.getPrimarykey(), patientID)){
                Drug drug=new Drug();
                drug.setDrugName(drugDto.drugName);
                drug.setDrugDose(drugDto.drugDose);
                drug.setPatientDrug(p);
                drugRepository.save(drug);
                p.getDrugList().add(drug);
                userService.patientRepository.save(p);
                return true;
            }
        }
        return false;
    }
    }

