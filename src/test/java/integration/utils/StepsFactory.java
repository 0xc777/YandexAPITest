package integration.utils;

import integration.tests.steps.assured.*;
import integration.tests.steps.interfaces.*;

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
