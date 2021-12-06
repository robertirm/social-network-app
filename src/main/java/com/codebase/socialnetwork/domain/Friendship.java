package com.codebase.socialnetwork.domain;

import java.time.LocalDateTime;
import static com.codebase.socialnetwork.utils.Constants.DATE_TIME_FORMATTER;

public class Friendship extends Entity<Tuple<Long, Long>> implements Comparable<Friendship> {
    LocalDateTime date;
    String status;

    public Friendship(LocalDateTime date) {
        this.date = date;
        this.status = "pending";
    }

    public LocalDateTime getDate(){
        return this.date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", date=" + date.format(DATE_TIME_FORMATTER) +
                ", status='" + status + '\'' +
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
