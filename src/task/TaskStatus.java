package task;

import java.util.Objects;

public class TaskStatus {
    private Boolean NEW;
    private Boolean DONE;
    private Boolean IS_PROGRESS;

    TaskStatus(Boolean NEW, Boolean DONE, Boolean IS_PROGRESS){
        this.NEW = NEW;
        this.DONE = DONE;
        this.IS_PROGRESS = IS_PROGRESS;
    }

    public static TaskStatus getNEW(){
        return new TaskStatus(true,false,false);
    }

    public static TaskStatus getDONE(){
        return new TaskStatus(false,true,false);
    }

    public static TaskStatus getIsProgress(){
        return new TaskStatus(false,false,true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskStatus that = (TaskStatus) o;
        return Objects.equals(NEW, that.NEW) && Objects.equals(DONE, that.DONE) && Objects.equals(IS_PROGRESS, that.IS_PROGRESS);
    }

    @Override
    public int hashCode() {
        return Objects.hash(NEW, DONE, IS_PROGRESS);
    }

    @Override
    public String toString() {
        String result = "";
        if (NEW==true){
            result = result + "NEW";
        }else if (DONE==true){
            result = result + "DONE";
        }else result = result + "IS_PROGRESS";
        return result;
    }

}


