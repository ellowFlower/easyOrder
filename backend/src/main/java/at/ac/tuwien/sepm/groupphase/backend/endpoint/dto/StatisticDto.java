package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class StatisticDto {
    Date start;
    Date end = new Date();
    List<ItemStatisticDto> foodStats = new ArrayList<ItemStatisticDto>();
    List<ItemStatisticDto> drinkStats = new ArrayList<ItemStatisticDto>();

    public StatisticDto(){
        // new Date() creates the current date and time.
        this.start = new Date();
        this.end = new Date();
    }

    public StatisticDto(Date start, Date end){
        this.start = start;
        this.end = end;
    }

    public StatisticDto(Date start, List<ItemStatisticDto> foodStats,
                        List<ItemStatisticDto> drinkStats) {
        this.start = start;
        this.foodStats = foodStats;
        this.drinkStats = drinkStats;
    }

    public StatisticDto(Date start, Date end, List<ItemStatisticDto> foodStats,
                        List<ItemStatisticDto> drinkStats) {
        this.start = start;
        this.end = end;
        this.foodStats = foodStats;
        this.drinkStats = drinkStats;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public List<ItemStatisticDto> getfoodStats() {
        return foodStats;
    }

    public void setfoodStats(List<ItemStatisticDto> foodStats) {
        this.foodStats = foodStats;
    }

    public List<ItemStatisticDto> getdrinkStats() {
        return drinkStats;
    }

    public void setdrinkStats(List<ItemStatisticDto> drinkStats) {
        this.drinkStats = drinkStats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatisticDto)) return false;
        StatisticDto that = (StatisticDto) o;
        return Objects.equals(start, that.start) &&
            Objects.equals(end, that.end) &&
            Objects.equals(foodStats, that.foodStats) &&
            Objects.equals(drinkStats, that.drinkStats);
    }

    @Override
    public int hashCode() { return Objects.hash(start, end, foodStats, drinkStats); }

    @Override
    public String toString() {
        return "{" +
            "\"start\": " + "\"" + start + "\"" +
            ", \"end\": " + "\"" + end + "\"" +
            ", \"foodStats\": " + foodStats +
            ", \"drinkStats\": " + drinkStats +
            "}";
    }

}
