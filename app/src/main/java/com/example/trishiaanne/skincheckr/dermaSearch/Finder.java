package com.example.trishiaanne.skincheckr.dermaSearch;

import android.app.Activity;
import android.content.res.AssetManager;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Finder {

    private final String DISTANCE_MATRIX = "https://maps.googleapis.com/maps/api/distancematrix/json?origins";
    private final String ORIGIN = "&origins=";
    private final String DESTINATION = "&destionations=";
    private final String API_KEY = "&key=AIzaSyAswjvzMmUmqp9EShBRdPoJPcXw18xdR4Q";
    private String CSV_PATH ="";
    private List<Dermatologist> dermaList;

    private int city;

    public Finder(String locality, Activity activity) {
        this.locality = locality;
        this.activity = activity;
        this.setDermaList(readCsv());
    }

    private String locality;
    private Activity activity;

    public Finder(int city, Activity activity) {
        this.city = city;
        this.activity = activity;
        chooseCity();
        this.setDermaList(readCsv());
    }


    private List<Dermatologist> readCsv() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(activity.getAssets().open(CSV_PATH)))) {
            CSVReader csv = new CSVReaderBuilder(reader).withSkipLines(1).build();

            List<String[]> records = csv.readAll();
            List<Dermatologist> dermaList = new ArrayList<>();

            ListIterator<String[]> li = records.listIterator(0);

            while (li.hasNext()) {
                String[] record = li.next();

                Dermatologist d = new Dermatologist();
                if (dermaList.isEmpty()) {
                    d.setId(Integer.parseInt(record[0]));
                    d.setName(record[1]);
                    d.setLocation(record[2]);
                    if (record[3].equals("null")) {
                        d.setYears(0);
                    } else {
                        d.setYears(Integer.parseInt(record[3]));
                    }
                    if (record[4].equals("null")) {
                        d.setFee("None");
                    } else {
                        d.setFee(record[4]);
                    }
                    d.setInfo(record[6]);
                    d.setClinic(record[7], record[8], record[9], record[11], record[12], record[13], record[14]);
                    if (record[10].equals("[]")) {
                        d.setService("None");
                    } else {
                        String regex = "\\[|\\{\\\"(services)\\\"\\:\\\"|\\\"\\}\\,|\\\"\\}\\]";
                        String[] services = record[10].split(regex);
                        for (String s : services) {
                            if (s.equals("")) {
                                continue;
                            } else {
                                d.setService(s);
                            }
                        }
                    }
                    dermaList.add(d.getId(), d);
                } else if (li.hasPrevious()) {
                    String[] prev = li.previous();
                    //if there are multiple clinics/schedules
                    if (li.previous()[0].equals(record[0])) {
                        d = dermaList.get(Integer.parseInt(record[0]));
                        List<Clinic> clinic = d.getClinics();
                        boolean addedSched = false;
                        for (Clinic c : clinic) {
                            //if more than one schedule for the same clinic
                            if (c.getName().equals(record[12]) && c.getLocation().equals(record[11])) {
                                d.setClinicSched(clinic.indexOf(c), record[8], record[9]);
                                addedSched = true;
                            }
                        }
                        //if different clinics
                        if (!addedSched) {
                            d.setClinic(record[7], record[8], record[9], record[11], record[12], record[13], record[14]);
                        }
                        dermaList.set(d.getId(), d);
                    } else {
                        d.setId(Integer.parseInt(record[0]));
                        d.setName(record[1]);
                        d.setLocation(record[2]);
                        if (record[3].equals("null")) {
                            d.setYears(0);
                        } else {
                            d.setYears(Integer.parseInt(record[3]));
                        }
                        if (record[4].equals("null")) {
                            d.setFee("None");
                        } else {
                            d.setFee(record[4]);
                        }
                        d.setInfo(record[6]);
                        d.setClinic(record[7], record[8], record[9], record[11], record[12], record[13], record[14]);
                        if (record[10].equals("[]")) {
                            d.setService("None");
                        } else {
                            String regex = "\\[|\\{\\\"(services)\\\"\\:\\\"|\\\"\\}\\,|\\\"\\}\\]";
                            String[] services = record[10].split(regex);
                            for (String s : services) {
                                if (s.equals("")) {
                                    continue;
                                } else {
                                    d.setService(s);
                                }
                            }
                        }
                        dermaList.add(d.getId(), d);
                    }
                    li.next();
                    li.next();
                }
            }

            return dermaList;
        } catch (IOException ex) {
            Logger.getLogger(Finder.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Cannot find file!");
        }

        return null;
    }

    public void findByFee(int choice) {
        switch(choice) {
            case 0:
                Collections.sort(getDermaList(), new SortByFeeLowToHigh());
                break;
            case 1:
                Collections.sort(getDermaList(), new SortByFeeHighToLow());
                break;
            default:
                return;
        }

//        printCsv();
    }

    public void findByYear() {
        Collections.sort(getDermaList(), new SortByYear());

//        printCsv();
    }

    //TODO: Distance Matrix sorting implementation

    public List<Dermatologist> getDermaList() {
        return dermaList;
    }

    public void setDermaList(List<Dermatologist> dermaList) {
        this.dermaList = dermaList;
    }

    class SortByFeeLowToHigh implements Comparator<Dermatologist> {

        @Override
        public int compare(Dermatologist d1, Dermatologist d2) {
            String fee1 = d1.getFee();
            String fee2 = d2.getFee();
            int price1, price2;
            if (fee1.startsWith("Free")) {
                price1 = 0;
            } else if (fee1.equals("None")) {
                price1 = 9999;
            } else {
                fee1 = fee1.replace(",", "");
                price1 = Integer.parseInt(fee1.substring(1));
            }
            if (fee2.startsWith("Free")) {
                price2 = 0;
            } else if (fee2.equals("None")) {
                price2 = 9999;
            } else {
                fee2 = fee2.replace(",", "");
                price2 = Integer.parseInt(fee2.substring(1));
            }
            if (price1 > price2) {
                return 1;
            } else if (price1 < price2) {
                return -1;
            } else {
                return 0;
            }
        }

    }

    class SortByFeeHighToLow implements Comparator<Dermatologist> {

        @Override
        public int compare(Dermatologist d1, Dermatologist d2) {
            String fee1 = d1.getFee();
            String fee2 = d2.getFee();
            int price1, price2;
            if (fee1.startsWith("Free")) {
                price1 = 1;
            } else if (fee1.equals("None")) {
                price1 = 0;
            } else {
                fee1 = fee1.replace(",", "");
                price1 = Integer.parseInt(fee1.substring(1));
            }
            if (fee2.startsWith("Free")) {
                price2 = 1;
            } else if (fee2.equals("None")) {
                price2 = 0;
            } else {
                fee2 = fee2.replace(",", "");
                price2 = Integer.parseInt(fee2.substring(1));
            }
            if (price2 > price1) {
                return 1;
            } else if (price2 < price1) {
                return -1;
            } else {
                return 0;
            }
        }

    }

    class SortByYear implements Comparator<Dermatologist> {

        @Override
        public int compare(Dermatologist d1, Dermatologist d2) {
            if(d2.getYears() > d1.getYears())
                return 1;
            else if (d2.getYears() < d1.getYears())
                return -1;
            else
                return 0;
        }

    }

    private void chooseCity() {
        switch (city) {
            case 0:
                CSV_PATH += "practo_caloocan.csv";
                break;
            case 1:
                CSV_PATH += "practo_laspinas.csv";
                break;
            case 2:
                CSV_PATH += "practo_makati.csv";
                break;
            case 3:
                CSV_PATH += "practo_malabon.csv";
                break;
            case 4:
                CSV_PATH += "practo_mandaluyong.csv";
                break;
            case 5:
                CSV_PATH += "practo_mnl.csv";
                break;
            case 6:
                CSV_PATH += "practo_muntinlupa.csv";
                break;
            case 7:
                CSV_PATH += "practo_paranaque.csv";
                break;
            case 8:
                CSV_PATH += "practo_pasay.csv";
                break;
            case 9:
                CSV_PATH += "practo_pasig.csv";
                break;
            case 10:
                CSV_PATH += "practo_pateros.csv";
                break;
            case 11:
                CSV_PATH += "practo_qc.csv";
                break;
            case 12:
                CSV_PATH += "practo_sanjuan.csv";
                break;
            case 13:
                CSV_PATH += "practo_taguig.csv";
                break;
            case 14:
                CSV_PATH += "practo_valenzuela.csv";
                break;
            default:
                break;
        }
    }

    private void chooseLocality() {
        switch(locality) {
            case "Manila":
                CSV_PATH += "practo_mnl.csv";
                break;
        }
    }
}
