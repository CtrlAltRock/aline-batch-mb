package com.smoothstack.transactionbatch.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import com.smoothstack.transactionbatch.model.StateBase;

public class StateGenerator {
    private static StateGenerator INSTANCE = null;
    private HashSet<String> history = new HashSet<>(); 
    private final Map<String, StateBase> states;

    private StateGenerator() {
        this.states = enrichStates();
    }

    public static StateGenerator getInstance() {
        if (INSTANCE == null) {
            synchronized (StateGenerator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StateGenerator();
                }
            }
        }

        return INSTANCE;
    }

    public Collection<StateBase> getStates() {
        return states.values();
    }

    public Optional<StateBase> getState(String abbrev) {
        if (!states.containsKey(abbrev)) return Optional.empty();

        if (history.contains(abbrev)) return Optional.empty();

        history.add(abbrev);

        return Optional.of(states.get(abbrev));
    }

    private static Map<String, StateBase> enrichStates() {
        Map<String, StateBase> states = new HashMap<>();

        states.put("AL", new StateBase(1, "AL", "Alabama", "Montgomery", "Heart of Dixie"));
        states.put("AK", new StateBase(2, "AK", "Alaska", "Juneau", "Last Frontier"));
        states.put("AZ", new StateBase(3, "AZ", "Arizona", "Phoenix", "Grand Canyon State"));
        states.put("AR", new StateBase(4, "AR", "Arkansas", "Little Rock", "Land of Opportunity"));
        states.put("CA", new StateBase(5, "CA", "California", "Sacramento", "Golden State"));
        states.put("CO", new StateBase(6, "CO", "Colorado", "Denver", "Centennial State"));
        states.put("CT", new StateBase(7, "CT", "Connecticut", "Hartford", "Constitution State"));
        states.put("DE", new StateBase(8, "DE", "Delaware", "Dover", "First State"));
        states.put("FL", new StateBase(9, "FL", "Florida", "Tallahassee", "Sunshine State"));
        states.put("GA", new StateBase(10, "GA", "Georgia", "Atlanta", "Peach State"));
        states.put("HI", new StateBase(11, "HI", "Hawaii", "Honolulu", "Aloha State"));
        states.put("ID", new StateBase(12, "ID", "Idaho", "Boise", "Gem State"));
        states.put("IL", new StateBase(13, "IL", "Illinois", "Springfield", "Prairie State"));
        states.put("IN", new StateBase(14, "IN", "Indiana", "Indianapolis", "Hoosier State"));
        states.put("IA", new StateBase(15, "IA", "Iowa", "Des Moines", "Hawkeye State"));
        states.put("KS", new StateBase(16, "KS", "Kansas", "Topeka", "Sunflower State"));
        states.put("KY", new StateBase(17, "KY", "Kentucky", "Frankfort", "Bluegrass State"));
        states.put("LA", new StateBase(18, "LA", "Louisiana", "Baton Rouge", "Pelican State"));
        states.put("ME", new StateBase(19, "ME", "Maine", "Augusta", "Pine Tree"));
        states.put("MD", new StateBase(20, "MD", "Maryland", "Annapolis", "Old Line State"));
        states.put("MA", new StateBase(21, "MA", "Massachusetts", "Boston", "Bay State"));
        states.put("MI", new StateBase(22, "MI", "Michigan", "Lansing", "Wolverine State"));
        states.put("MN", new StateBase(23, "MN", "Minnesota", "St. Paul", "Land of 10,000 Lakes"));
        states.put("MS", new StateBase(24, "MS", "Mississippi", "Jackson", "Magnolia State"));
        states.put("MO", new StateBase(25, "MO", "Missouri", "Jefferson City", "Show Me State"));
        states.put("MT", new StateBase(26, "MT", "Montana", "Helena", "Treasure State"));
        states.put("NE", new StateBase(27, "NE", "Nebraska", "Lincoln", "Cornhusker State"));
        states.put("NV", new StateBase(28, "NV", "Nevada", "Carson City", "Silver State"));
        states.put("NH", new StateBase(29, "NH", "New Hampshire", "Concord", "Granite State"));
        states.put("NJ", new StateBase(30, "NJ", "New Jersey", "Trenton", "Garden State"));
        states.put("NM", new StateBase(31, "NM", "New Mexico", "Santa Fe", "Land of Enchantment"));
        states.put("NY", new StateBase(32, "NY", "New York", "Albany", "Empire State"));
        states.put("NC", new StateBase(33, "NC", "North Carolina", "Raleigh", "Tar Heel State"));
        states.put("ND", new StateBase(34, "ND", "North Dakota", "Bismarck", "Flickertail State"));
        states.put("OH", new StateBase(35, "OH", "Ohio", "Columbus", "Buckeye State"));
        states.put("OK", new StateBase(36, "OK", "Oklahoma", "Oklahoma City", "Sooner State"));
        states.put("OR", new StateBase(37, "OR", "Oregon", "Salem", "Beaver State"));
        states.put("PA", new StateBase(38, "PA", "Pennsylvania", "Harrisburg", "Keystone State"));
        states.put("RI", new StateBase(39, "RI", "Rhode Island", "Providence", "Little Rhody"));
        states.put("SC", new StateBase(40, "SC", "South Carolina", "Columbia", "Palmetto State"));
        states.put("SD", new StateBase(41, "SD", "South Dakota", "Pierre", "Coyote State"));
        states.put("TN", new StateBase(42, "TN", "Tennessee", "Nashville", "Volunteer State"));
        states.put("TX", new StateBase(43, "TX", "Texas", "Austin", "Lone Star State"));
        states.put("UT", new StateBase(44, "UT", "Utah", "Salt Lake City", "Beehive State"));
        states.put("VT", new StateBase(45, "VT", "Vermont", "Montpelier", "Green Mountain"));
        states.put("VA", new StateBase(46, "VA", "Virginia", "Richmond", "Mother of Presidents"));
        states.put("WA", new StateBase(47, "WA", "Washington", "Olympia", "Evergreen State"));
        states.put("WV", new StateBase(48, "WV", "West Virginia", "Charleston", "Mountain State"));
        states.put("WI", new StateBase(49, "WI", "Wisconsin", "Madison", "Badger State"));
        states.put("WY", new StateBase(50, "WY", "Wyoming", "Cheyenne", "Equality State"));
                
        return states;
    }   
}
