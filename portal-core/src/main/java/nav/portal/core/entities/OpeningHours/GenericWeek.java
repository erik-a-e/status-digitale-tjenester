package nav.portal.core.entities.OpeningHours;

import nav.portal.core.entities.AreaEntity;

import java.sql.Time;
import java.util.Objects;
import java.util.UUID;

public class GenericWeek {
    private UUID id;
    private String name;
    private final int[] days = new int[7];
    private Time openingHour;
    private Time closingHour;

    public GenericWeek() {
    }

    public GenericWeek(UUID id, String name, int[] days, Time openingHour, Time closingHour) {
        this.id = id;
        this.name = name;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        System.arraycopy(days, 0, this.days, 0, days.length);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getDays(){
        int[]selectedDays = new int[0];
        System.arraycopy(days, 0, selectedDays, 0, days.length);
        return selectedDays;
    }

    public Time getOpeningHour(){ return openingHour; }

    public Time getClosingHour(){ return closingHour; }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDays(int[] days){
        System.arraycopy(days, 0, this.days, 0, days.length);
    }

    public void setOpeningHour(Time openingHour){
        this.openingHour = openingHour;
    }

    public void setClosingHour(Time closingHour)    {
        this.closingHour = closingHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericWeek that = (GenericWeek) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(openingHour, that.openingHour)
                && Objects.equals(closingHour, that.closingHour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, openingHour, closingHour);
    }
}
