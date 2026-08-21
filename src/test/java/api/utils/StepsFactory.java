package api.utils;

import api.tests.steps.assured.*;
import api.tests.steps.interfaces.*;

public class StepsFactory {
    public static FileSteps getFileSteps(){
        return new FileStepsAssured();
    }
    public static FolderSteps getFolderSteps(){
        return new FolderStepsAssured();
    }
    public  static PublishSteps getPublishSteps(){
        return new PublishStepsAssured();
    }
    public  static ResourceSteps getResourceSteps(){
        return new ResourceStepsAssured();
    }
    public  static TrashSteps getTrashSteps(){
        return new TrashStepsAssured();
    }
}
