package socialNetwork.domain;

import java.time.LocalDateTime;
import static socialNetwork.utils.Constants.DATE_TIME_FORMATTER;

public class Friendship extends Entity<Tuple<Long, Long>> implements Comparable<Friendship>{

    LocalDateTime date;

    public Friendship(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate(){
        return this.date;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", date=" + date.format(DATE_TIME_FORMATTER) +
                '}';
    }

    @Override
    public int compareTo(Friendship o) {
        if(this.equals(o)){
            return 0;
        }
        if(this.hashCode() > o.hashCode()){
            return 1;
        }
        return -1;
    }
}
