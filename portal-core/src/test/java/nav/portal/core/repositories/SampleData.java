package nav.portal.core.repositories;

import nav.portal.core.entities.*;
import nav.portal.core.enums.OpsMessageSeverity;
import nav.portal.core.enums.ServiceStatus;
import nav.portal.core.enums.ServiceType;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;


public class SampleData {

    static final ArrayList<String> areaNames = new ArrayList<>(Arrays.asList("Arbeidsøker eller permitert", "Pensjon", "Venter barn", "Alene med barn", "Sykmeldt",
            "Skal søke AAP", "Har mistet noen i nær famile", "Sykdom i familien", "Trenger tilrettelegging",
            "Trenger økonomisk sosialhjelp", "Trenger økonomisk rådgivning", "Berørt av EØS-saken", "Ett navn til", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at"));

    static final ArrayList<String> dashboardNames = new ArrayList<>(Arrays.asList("Privatperson", "Arbeidsgiver", "Sammarbeidspartner", "Et ganske så langt navn kommer her, går dette an da?", "ÆØÅ", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at"));

    static final ArrayList<String> serviceNames = new ArrayList<>(Arrays.asList("Service1", "Testesrvice 2", "Æ er en tjeneste", "Øgletjeneste", "tjeneste123", "tjeneste213", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at"));

    static final ArrayList<String> urlStrings = new ArrayList<>(Arrays.asList("https://www.nrk.no", "https://www.nrk.no/nyheter/", "https://wwww.123abc.com", "https://wwww.ab.no", "https://wwww.ac.no", "https://wwww.ad.no", "https://wwww.ae.no", "https://wwww.af", "https://wwww.ag", "https://wwww.ah.no", "https://wwww.ai.no", "https://wwww.aj.no", "https://wwww.ak.no", "https://wwww.al.no", "https://wwww.am.no", "https://wwww.an.no", "https://wwww.ao.no"));

    static final ArrayList<String> teamNames = new ArrayList<>(Arrays.asList("team1", "teamOrlene", "teamÆØÅ", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at"));

    static final ArrayList<String> descriptions = new ArrayList<>(Arrays.asList("kort beskrivelse", "laaang beskrivelse ------- laaang beskrivelse -------laaang beskrivelse -------laaang beskrivelse -------laaang beskrivelse -------laaang beskrivelse -------laaang beskrivelse -------laaang beskrivelse -------laaang beskrivelse -------", "beskrivelseÆØÅ"));

    static final ArrayList<String> icons = new ArrayList<>(Arrays.asList("0001", "0002", "0003", "0004", "0005", "0006", "0007", "0008", "0009", "0010", "0011", "0012"));

    static final ArrayList<String> firstNames = new ArrayList<>(Arrays.asList("Arne", "Bodil", "Gudrun", "Kjell Åge", "Hufsa", "Elg", "Rake", "Æskild", "Øygunn"));
    static final ArrayList<String> headersForOpsMessages = new ArrayList<>(Arrays.asList("Trøbbel i tårnet", "Nå går det gæli", "Spark meg baklengs oppi fuglekassa", "For the memes", "Det blåser nordavind fra alle kanter"));
    static final ArrayList<String> infoTextForOpsMessages = new ArrayList<>(Arrays.asList("Noen har gjort noe alvorlig galt", "En ape har trengt seg inn på systemet. Det ligger bananer overalt", "WW3, oh no", "Facebook har sendt jorda inn i sola", "Elon Musk har kjøpt opp Nav"));
    static final ArrayList<OpsMessageSeverity> opsMessageSeverity = new ArrayList<>(Arrays.asList(OpsMessageSeverity.DOWN, OpsMessageSeverity.OK,OpsMessageSeverity.ISSUE,OpsMessageSeverity.NEUTRAL));
    static final ArrayList<String> maintenanceDescriptions = new ArrayList<>(Arrays.asList("Fixing the trøbbel i tårnet", "Supporting those som går gæli", "Mending the fuglekassa", "Taming memes", "Upkeep av nordavind fra alle kanter"));

    static final ArrayList<String> rules = new ArrayList<>(Arrays.asList("06.04.2023 ? ? 00:00-00:00","??.??.???? 1-5,10-L ? 07:00-21:00","24.12.???? ? 1-5 09:00-14:00"));


    public static String getRandomizedDashboardName() {
        return getRandomFromArray(dashboardNames);
    }

    public static AreaEntity getRandomizedAreaEntity() {
        return new AreaEntity()
                .setName(getRandomFromArray(areaNames))
                .setDescription(getRandomFromArray(descriptions))
                .setContains_components(false);
    }

    public static OpsMessageEntity getRandomOpsMessageEntity() {
        Random random = new Random();
        return new OpsMessageEntity()
                .setInternalHeader(getRandomFromArray(headersForOpsMessages))
                .setInternalText(getRandomFromArray(infoTextForOpsMessages))
                .setStartTime(getZonedDateTimeNowWithOutDecimals())
                .setEndTime(getZonedDateTimeNowWithOutDecimals().plusDays(14))
                .setSeverity(getRandomOpsMessageSeverity())
                .setOnlyShowForNavEmployees(random.nextBoolean());
    }

    private static ZonedDateTime getZonedDateTimeNowWithOutDecimals(){
        return ZonedDateTime.of(LocalDate.now(),LocalTime.of(0,0),ZoneId.of("Europe/Oslo"));
    }

    public static List<OpsMessageEntity> getNonEmptyListOfOpsMessageEntity(int numberOfOpsMessages) {
        List<OpsMessageEntity> opsMessages = new ArrayList<>();
        for (int i = 0; i < numberOfOpsMessages; i++) {
            opsMessages.add(getRandomOpsMessageEntity());
        }
        return opsMessages;
    }

    public static AreaEntity getRandomizedAreaEntityWithNameNotInList(List<AreaEntity> areas) {
        List<String> usedNames = areas.stream().map(AreaEntity::getName).collect(Collectors.toList());
        ArrayList<String> possibleNames = new ArrayList<>(areaNames);
        possibleNames.removeAll(usedNames);
        return new AreaEntity()
                .setName(getRandomFromArray(possibleNames))
                .setDescription(getRandomFromArray(descriptions))
                .setContains_components(false);
    }

    public static OpeningHoursRuleEntity getRandomizedOpeningRule() {
        return new OpeningHoursRuleEntity()
                .setName(getRandomFromArray(areaNames))
                .setRule(getRandomFromArray(rules));
    }

    public static List<OpeningHoursRuleEntity> getNonEmptyListOfOpeningRules(int length) {
        int numberOfRules = length;
        List<OpeningHoursRuleEntity> OpeningHoursRules = new ArrayList<>();
        for (int i = 0; i < numberOfRules; i++) {
            OpeningHoursRules.add(getRandomizedOpeningHoursRuleEntityWithNameNotInList(OpeningHoursRules));
        }
        return OpeningHoursRules;
    }

    public static OpeningHoursRuleEntity getRandomizedOpeningHoursRuleEntityWithNameNotInList(List<OpeningHoursRuleEntity> OpeningHoursRules) {
        List<String> usedNames = OpeningHoursRules.stream().map(OpeningHoursRuleEntity::getName).collect(Collectors.toList());
        ArrayList<String> possibleNames = new ArrayList<>(areaNames);
        possibleNames.removeAll(usedNames);
        return new OpeningHoursRuleEntity()
                .setName(getRandomFromArray(possibleNames))
                .setRule(getRandomFromArray(rules));
    }


    public static List<AreaEntity> getRandomLengthListOfAreaEntity() {
        Random random = new Random();
        int numberOfAreas = 1 + random.nextInt(12);
        List<AreaEntity> areas = new ArrayList<>();
        for (int i = 0; i <= numberOfAreas; i++) {
            areas.add(getRandomizedAreaEntityWithNameNotInList(areas));
        }
        return areas;
    }

    public static List<AreaEntity> getNonEmptyListOfAreaEntity(int length) {
        int numberOfAreas = length;
        List<AreaEntity> areas = new ArrayList<>();
        for (int i = 0; i < numberOfAreas; i++) {
            areas.add(getRandomizedAreaEntityWithNameNotInList(areas));
        }
        return areas;
    }

    public static SubAreaEntity getRandomizedSubAreaEntity() {
        return new SubAreaEntity()
                .setName(getRandomFromArray(areaNames));
    }

    public static SubAreaEntity getRandomizedSubAreaEntityWithNameNotInList(List<SubAreaEntity> subAreas) {
        List<String> usedNames = subAreas.stream().map(SubAreaEntity::getName).collect(Collectors.toList());
        ArrayList<String> possibleNames = new ArrayList<>(areaNames);
        possibleNames.removeAll(usedNames);
        return new SubAreaEntity()
                .setName(getRandomFromArray(possibleNames));
    }

    public static List<SubAreaEntity> getRandomLengthListOfSubAreaEntity() {
        Random random = new Random();
        int numberOfSubAreas = random.nextInt(12);
        List<SubAreaEntity> subAreas = new ArrayList<>();
        for (int i = 0; i <= numberOfSubAreas; i++) {
            subAreas.add(getRandomizedSubAreaEntityWithNameNotInList(subAreas));
        }
        return subAreas;
    }

    public static List<SubAreaEntity> getRandomLengthNonEmptyListOfSubAreaEntity() {
        Random random = new Random();
        int numberOfSubAreas = 1 + random.nextInt(12);
        List<SubAreaEntity> subAreas = new ArrayList<>();
        for (int i = 0; i <= numberOfSubAreas; i++) {
            subAreas.add(getRandomizedSubAreaEntityWithNameNotInList(subAreas));
        }
        return subAreas;
    }

    public static List<SubAreaEntity> getNonEmptyListOfSubAreaEntity(int length) {
        Random random = new Random();
        int numberOfSubAreas = length;
        List<SubAreaEntity> subAreas = new ArrayList<>();
        for (int i = 0; i < numberOfSubAreas; i++) {
            subAreas.add(getRandomizedSubAreaEntityWithNameNotInList(subAreas));
        }
        return subAreas;
    }

    public static ServiceEntity getRandomizedServiceEntity() {
        return new ServiceEntity()
                .setName(getRandomFromArray(serviceNames))
                .setType(getRandomServiceType())
                .setTeam(getRandomFromArray(teamNames))
                .setStatusNotFromTeam(Boolean.FALSE)
                .setMonitorlink(getRandomFromArray(urlStrings))
                .setPollingOnPrem(false);
    }

    public static ServiceEntity getRandomizedServiceEntityWithNameNotInList(List<ServiceEntity> services) {
        List<String> usedNames = services.stream().map(ServiceEntity::getName).collect(Collectors.toList());
        ArrayList<String> possibleNames = new ArrayList<>(serviceNames);
        possibleNames.removeAll(usedNames);
        return new ServiceEntity()
                .setName(getRandomFromArray(possibleNames))
                .setType(ServiceType.TJENESTE)
                .setTeam(getRandomFromArray(teamNames))
                .setPolling_url(getRandomFromArray(urlStrings))
                .setStatusNotFromTeam(Boolean.FALSE)
                .setMonitorlink(getRandomFromArray(urlStrings))
                .setPollingOnPrem(false);
    }

    public static List<ServiceEntity> getRandomLengthListOfServiceEntity() {
        Random random = new Random();
        int numberOfAreas = random.nextInt(12);
        List<ServiceEntity> services = new ArrayList<>();
        for (int i = 0; i < numberOfAreas; i++) {
            services.add(getRandomizedServiceEntityWithNameNotInList(services));
        }
        return services;
    }

    public static List<ServiceEntity> getRandomLengthNonEmptyListOfServiceEntity() {
        Random random = new Random();
        int numberOfServices = 1 + random.nextInt(12);
        List<ServiceEntity> services = new ArrayList<>();
        for (int i = 0; i < numberOfServices; i++) {
            services.add(getRandomizedServiceEntityWithNameNotInList(services));
        }
        return services;
    }

    public static List<ServiceEntity> getNonEmptyListOfServiceEntity(int numberOfServices) {
        List<ServiceEntity> services = new ArrayList<>();
        for (int i = 0; i < numberOfServices; i++) {
            services.add(getRandomizedServiceEntityWithNameNotInList(services));
        }
        return services;
    }

    public static List<ServiceEntity> getNonEmptyListOfServiceEntityWithUid(int numberOfServices) {
        List<ServiceEntity> result = getNonEmptyListOfServiceEntity(numberOfServices);
        result.forEach(service -> service.setId(UUID.randomUUID()));
        return result;
    }


    private static String getRandomFromArray(ArrayList<String> array) {
        if (array.size() == 0) {
            //Hit skal man ikke komme
            return null;
        }
        Random random = new Random();
        return array.get(random.nextInt(array.size()));
    }

    private static OpsMessageSeverity getRandomOpsMessageSeverity() {
        Random random = new Random();
        return opsMessageSeverity.get(random.nextInt(opsMessageSeverity.size()));
    }

    private static ServiceType getRandomServiceType() {
        Random random = new Random();
        return ServiceType.values()[random.nextInt(ServiceType.values().length)];
    }

    public static List<String> getDashboardNames() {
        return dashboardNames;
    }

    private static ServiceStatus getRandomServiceStatus() {
        Random random = new Random();
        return ServiceStatus.values()[random.nextInt(ServiceStatus.values().length)];
    }

    private static Integer getRandomResponseTime() {
        List<Integer> responseTime = Arrays.asList(10, 20, 30, 100, 1000);
        Random random = new Random();
        return responseTime.get(random.nextInt(responseTime.size()));
    }

    public static RecordEntity getRandomizedRecordEntity() {
        return new RecordEntity()
                .setCreated_at(ZonedDateTime.now())
                .setStatus(getRandomServiceStatus())
                .setResponsetime(getRandomResponseTime());
    }

    public static RecordEntity getRandomizedRecordEntityForService(ServiceEntity serviceEntity) {
        return new RecordEntity()
                .setServiceId(serviceEntity.getId())
                .setCreated_at(ZonedDateTime.now())
                .setStatus(getRandomServiceStatus())
                .setResponsetime(getRandomResponseTime());
    }

    public static List<RecordEntity> getRandomizedRecordEntitiesForService(ServiceEntity serviceEntity) {
        List<RecordEntity> records = new ArrayList<>();
        Random random = new Random();
        int numberOfRecords = 1 + random.nextInt(12);
        for (int i = 0; i < numberOfRecords; i++) {
            records.add(getRandomizedRecordEntityForService(serviceEntity));
        }
        return records;
    }

    public static DailyStatusAggregationForServiceEntity getRandomizedDailyStatusAggregationForService(ServiceEntity serviceEntity) {
        return new DailyStatusAggregationForServiceEntity()
                .setService_id(serviceEntity.getId())
                .setNumber_of_status_down(new Random().nextInt(3))
                .setNumber_of_status_issue(new Random().nextInt(2))
                .setNumber_of_status_ok(new Random().nextInt(100))
                .setAggregation_date(LocalDate.now());
    }



    public static MaintenanceEntity getRandomizedMaintenanceEntity() {
        Random random = new Random();
        int numberOfDays = random.nextInt(2);
        return new MaintenanceEntity()
                .setCreated_at(ZonedDateTime.now())
                .setDescription(getRandomFromArray(maintenanceDescriptions))
                .setStart_time(ZonedDateTime.now().plusDays(numberOfDays))
                .setEnd_time(ZonedDateTime.now().plusDays(numberOfDays + 2));
    }
}

