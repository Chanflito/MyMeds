package MyMeds.App;

public enum RecipeStatus {
    //Approved ==> Ya se creó la receta
    //Declined ==> Rechazada por el doctor.
    //In_progress ==> en proceso
    //Dispensed ==> Entregada
    //REJECTED ==>Rechazada por la farmacia.(para contemplar el caso de que no venga el paciente nunca etc.)
    APPROVED,DECLINED,IN_PROGRESS,DISPENSED,REJECTED
}
