package com.puente.service.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Map.entry;

@Component
@Getter
@Setter
public class CodigoPaisIsoDto {
    private Map<String, String> dictionary;

    public CodigoPaisIsoDto() {
        this.dictionary = Map.ofEntries(
                entry("AFG", "AF"),
                entry("ALA", "AX"),
                entry("ALB", "AL"),
                entry("DZA", "DZ"),
                entry("ASM", "AS"),
                entry("AND", "AD"),
                entry("AGO", "AO"),
                entry("AIA", "AI"),
                entry("ATA", "AQ"),
                entry("ATG", "AG"),
                entry("ARG", "AR"),
                entry("ARM", "AM"),
                entry("ABW", "AW"),
                entry("AUS", "AU"),
                entry("AUT", "AT"),
                entry("AZE", "AZ"),
                entry("BHS", "BS"),
                entry("BHR", "BH"),
                entry("BGD", "BD"),
                entry("BRB", "BB"),
                entry("BLR", "BY"),
                entry("BEL", "BE"),
                entry("BLZ", "BZ"),
                entry("BEN", "BJ"),
                entry("BMU", "BM"),
                entry("BTN", "BT"),
                entry("BOL", "BO"),
                entry("BES", "BQ"),
                entry("BIH", "BA"),
                entry("BWA", "BW"),
                entry("BVT", "BV"),
                entry("BRA", "BR"),
                entry("IOT", "IO"),
                entry("BRN", "BN"),
                entry("BGR", "BG"),
                entry("BFA", "BF"),
                entry("BDI", "BI"),
                entry("CPV", "CV"),
                entry("KHM", "KH"),
                entry("CMR", "CM"),
                entry("CAN", "CA"),
                entry("CYM", "KY"),
                entry("CAF", "CF"),
                entry("TCD", "TD"),
                entry("CHL", "CL"),
                entry("CHN", "CN"),
                entry("CXR", "CX"),
                entry("CCK", "CC"),
                entry("COL", "CO"),
                entry("COM", "KM"),
                entry("COD", "CD"),
                entry("COG", "CG"),
                entry("COK", "CK"),
                entry("CRI", "CR"),
                entry("HRV", "HR"),
                entry("CUB", "CU"),
                entry("CUW", "CW"),
                entry("CYP", "CY"),
                entry("CZE", "CZ"),
                entry("DNK", "DK"),
                entry("DJI", "DJ"),
                entry("DMA", "DM"),
                entry("DOM", "DO"),
                entry("ECU", "EC"),
                entry("EGY", "EG"),
                entry("SLV", "SV"),
                entry("GNQ", "GQ"),
                entry("ERI", "ER"),
                entry("EST", "EE"),
                entry("ETH", "ET"),
                entry("FLK", "FK"),
                entry("FRO", "FO"),
                entry("FJI", "FJ"),
                entry("FIN", "FI"),
                entry("FRA", "FR"),
                entry("GUF", "GF"),
                entry("PYF", "PF"),
                entry("ATF", "TF"),
                entry("GAB", "GA"),
                entry("GMB", "GM"),
                entry("GEO", "GE"),
                entry("DEU", "DE"),
                entry("GHA", "GH"),
                entry("GIB", "GI"),
                entry("GRC", "GR"),
                entry("GRL", "GL"),
                entry("GRD", "GD"),
                entry("GLP", "GP"),
                entry("GUM", "GU"),
                entry("GTM", "GT"),
                entry("GGY", "GG"),
                entry("GIN", "GN"),
                entry("GNB", "GW"),
                entry("GUY", "GY"),
                entry("HTI", "HT"),
                entry("HMD", "HM"),
                entry("VAT", "VA"),
                entry("HND", "HN"),
                entry("HKG", "HK"),
                entry("HUN", "HU"),
                entry("ISL", "IS"),
                entry("IND", "IN"),
                entry("IDN", "ID"),
                entry("IRN", "IR"),
                entry("IRQ", "IQ"),
                entry("IRL", "IE"),
                entry("IMN", "IM"),
                entry("ISR", "IL"),
                entry("ITA", "IT"),
                entry("JAM", "JM"),
                entry("JPN", "JP"),
                entry("JEY", "JE"),
                entry("JOR", "JO"),
                entry("KAZ", "KZ"),
                entry("KEN", "KE"),
                entry("KIR", "KI"),
                entry("PRK", "KP"),
                entry("KOR", "KR"),
                entry("KWT", "KW"),
                entry("KGZ", "KG"),
                entry("LAO", "LA"),
                entry("LVA", "LV"),
                entry("LBN", "LB"),
                entry("LSO", "LS"),
                entry("LBR", "LR"),
                entry("LBY", "LY"),
                entry("LIE", "LI"),
                entry("LTU", "LT"),
                entry("LUX", "LU"),
                entry("MAC", "MO"),
                entry("MDG", "MG"),
                entry("MWI", "MW"),
                entry("MYS", "MY"),
                entry("MDV", "MV"),
                entry("MLI", "ML"),
                entry("MLT", "MT"),
                entry("MHL", "MH"),
                entry("MTQ", "MQ"),
                entry("MRT", "MR"),
                entry("MUS", "MU"),
                entry("MYT", "YT"),
                entry("MEX", "MX"),
                entry("FSM", "FM"),
                entry("MDA", "MD"),
                entry("MCO", "MC"),
                entry("MNG", "MN"),
                entry("MNE", "ME"),
                entry("MSR", "MS"),
                entry("MAR", "MA"),
                entry("MOZ", "MZ"),
                entry("MMR", "MM"),
                entry("NAM", "NA"),
                entry("NRU", "NR"),
                entry("NPL", "NP"),
                entry("NLD", "NL"),
                entry("NCL", "NC"),
                entry("NZL", "NZ"),
                entry("NIC", "NI"),
                entry("NER", "NE"),
                entry("NGA", "NG"),
                entry("NIU", "NU"),
                entry("NFK", "NF"),
                entry("MKD", "MK"),
                entry("MNP", "MP"),
                entry("NOR", "NO"),
                entry("OMN", "OM"),
                entry("PAK", "PK"),
                entry("PLW", "PW"),
                entry("PSE", "PS"),
                entry("PAN", "PA"),
                entry("PNG", "PG"),
                entry("PRY", "PY"),
                entry("PER", "PE"),
                entry("PHL", "PH"),
                entry("PCN", "PN"),
                entry("POL", "PL"),
                entry("PRT", "PT"),
                entry("PRI", "PR"),
                entry("QAT", "QA"),
                entry("REU", "RE"),
                entry("ROU", "RO"),
                entry("RUS", "RU"),
                entry("RWA", "RW"),
                entry("BLM", "BL"),
                entry("SHN", "SH"),
                entry("KNA", "KN"),
                entry("LCA", "LC"),
                entry("MAF", "MF"),
                entry("SPM", "PM"),
                entry("VCT", "VC"),
                entry("WSM", "WS"),
                entry("SMR", "SM"),
                entry("STP", "ST"),
                entry("SAU", "SA"),
                entry("SEN", "SN"),
                entry("SRB", "RS"),
                entry("SYC", "SC"),
                entry("SLE", "SL"),
                entry("SGP", "SG"),
                entry("SXM", "SX"),
                entry("SVK", "SK"),
                entry("SVN", "SI"),
                entry("SLB", "SB"),
                entry("SOM", "SO"),
                entry("ZAF", "ZA"),
                entry("SGS", "GS"),
                entry("SSD", "SS"),
                entry("ESP", "ES"),
                entry("LKA", "LK"),
                entry("SDN", "SD"),
                entry("SUR", "SR"),
                entry("SJM", "SJ"),
                entry("SWE", "SE"),
                entry("CHE", "CH"),
                entry("SYR", "SY"),
                entry("TWN", "TW"),
                entry("TJK", "TJ"),
                entry("TZA", "TZ"),
                entry("THA", "TH"),
                entry("TLS", "TL"),
                entry("TGO", "TG"),
                entry("TKL", "TK"),
                entry("TON", "TO"),
                entry("TTO", "TT"),
                entry("TUN", "TN"),
                entry("TUR", "TR"),
                entry("TKM", "TM"),
                entry("TCA", "TC"),
                entry("TUV", "TV"),
                entry("UGA", "UG"),
                entry("UKR", "UA"),
                entry("ARE", "AE"),
                entry("GBR", "GB"),
                entry("USA", "US"),
                entry("UMI", "UM"),
                entry("URY", "UY"),
                entry("UZB", "UZ"),
                entry("VUT", "VU"),
                entry("VEN", "VE"),
                entry("VNM", "VN"),
                entry("VGB", "VG"),
                entry("VIR", "VI"),
                entry("WLF", "WF"),
                entry("ESH", "EH"),
                entry("YEM", "YE"),
                entry("ZMB", "ZM"),
                entry("ZWE", "ZW")
        );
    }
}
