package api.utils;

import api.tests.steps.assured.*;
import api.tests.steps.interfaces.*;

public class StepsFactory {
    public static FileSteps getFileStepsAssured(){
        return new FileStepsAssured();
    }
    public static FolderSteps getFolderStepsAssured(){
        return new FolderStepsAssured();
    }
    public  static PublishSteps getPublishStepsAssured(){
        return new PublishStepsAssured();
    }
    public  static ResourceSteps getResourceStepsAssured(){
        return new ResourceStepsAssured();
    }
    public  static TrashSteps getTrashStepsAssured(){
        return new TrashStepsAssured();
    }
}
