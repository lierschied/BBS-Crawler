package de.crawler;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.UserIdentity;
import com.prosysopc.ua.client.*;
import com.prosysopc.ua.stack.builtintypes.*;
import com.prosysopc.ua.stack.core.*;
import com.prosysopc.ua.stack.transport.security.SecurityMode;
import de.crawler.opc.Listener;
import de.crawler.opc.Station;
import de.crawler.utils.Colorize;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length >= 1) {
            String command = args[0];

            switch (command) {
                case "files" -> {
                    assert !args[1].equals("") : "No directory path specified";
                    parseFiles(args[1]);
                }
                case "stream" -> {
                    assert !args[1].equals("") : "No station to connect to specified";
                    parseStream(args[1]);
                }
                case "test" -> test();
                case "help" -> printHelp();
                default -> {
                    System.out.println(Colorize.red(String.format("Unknown command: %s", command)));
                    printHelp();
                }
            }
        } else {
            System.out.println(Colorize.red("Too few arguments to function\n"));
            printHelp();
        }
    }

    private static void test() {
        Station station = Station.Test;
        try {
            UaClient client = new UaClient(station.getUrl());
            client.setSecurityMode(SecurityMode.NONE);
            if (station != Station.Test) {
                client.setUserIdentity(new UserIdentity("MES", "training"));
            }

            ApplicationDescription applicationDescription = new ApplicationDescription();
            applicationDescription.setApplicationName(new LocalizedText("Sample Client", Locale.US));
            applicationDescription.setApplicationType(ApplicationType.Client);
            ApplicationIdentity identity = new ApplicationIdentity();
            identity.setApplicationDescription(applicationDescription);
            client.setApplicationIdentity(identity);

            EndpointDescription[] discoverEndpoints = client.discoverEndpoints();

            if (station == Station.Test) {
                client.setEndpoint(discoverEndpoints[11]);
            }

            Subscription subscription = new Subscription();
            MonitoredDataItem[] items = getItems();
            subscription.addItems(items);
            subscription.addNotificationListener(new Listener());
            client.addSubscription(subscription);

            client.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static MonitoredDataItem[] getItems() {
        String[] sensors = {
                "\"+AL-BG2_Bandsensor_Rechts\"",
                "\"+AL-BG1_Bandsensor_Links\"",
                "\"+AL-BG4_Stopper_Rechts\"",
                "\"+AL-BG3_Stopper_Links\"",
                "\"+AO-BG1_PosLinks\"",
                "\"+AO-BG2_PosRechts\"",
                "\"+AO-BG3_StopperLinks\"",
                "\"+AO-BG4_StopperRechts\"",
                "\"+AO-BG5_VereinzelerOffen\"",
                "\"+AO-BG6_HubUnten\"",
                "\"+AO-BG7_HubMitte\"",
                "\"+AO-BG8_HubOben\"",
                "\"+AM-BG1_PosLinks\"",
                "\"+AM-BG2_PosRechts\"",
                "\"+AM-BG3_StopperLinks\"",
                "\"+AM-BG4_StopperRechts\"",
                "\"+AM-BG5_VereinzelerOffen\"",
                "\"+AM-BG6_HubUnten\"",
                "\"+AM-BG7_HubMitte\"",
                "\"+AM-BG8_HubOben\"",
                "\"+AN-BG1_InduktivUnten\"",
                "\"+AM-BL1_Füllstand\"",
                "\"+AO-BL2_Füllstand\"",
                "\"+AL-MA1.Rechtslauf\"",
                "\"+AL-MA1.Linkslauf\"",
                "\"+AL-MB2_Stopper_Rechts\"",
                "\"+AL-MB1_Stopper_Links\"",
                "\"+AL-MA1.Vmax\"",
                "\"+AO-MB1_StopperLinks\"",
                "\"+AO-MB2_StopperRechts\"",
                "\"+AO-MB3_Vereinzeler\"",
                "\"+AO-MB4_HubUnten\"",
                "\"+AO-MB5_HubOben\"",
                "\"+AM-MB1_StopperLinks\"",
                "\"+AM-MB2_StopperRechts\"",
                "\"+AM-MB3_Vereinzeler\"",
                "\"+AM-MB4_HubUnten\"",
                "\"+AM-MB5_HubOben\"",
        };

        return Arrays.stream(sensors).map(s -> {
            NodeId nodeId = new NodeId(3, s);
            return new MonitoredDataItem(nodeId);
        }).toArray(MonitoredDataItem[]::new);
    }

    private static void parseStream(String station) throws Exception {
        new StreamParser(station).start();
    }

    private static void parseFiles(String dirPath) {
        new FileParser(dirPath).start();
    }

    private static void printHelp() {
        System.out.println(Colorize.green("Available commands:\n"));
        String[] commands = {
                String.format("files {%s} | %s", Colorize.yellow("pathToFiles"),
                        Colorize.blue("parses sensors out of files within specified directory")),
                String.format("stream {%s} | %s", Colorize.yellow("station"),
                        Colorize.blue("connects to a station an parses sensors")),
        };

        for (String line : commands) {
            System.out.println(line);
        }
    }

    public static String firstGroupMatch(Pattern p, String toMatch) {
        Matcher m = p.matcher(toMatch);

        if (m.find()) {
            return m.group(1);
        }

        return "no match found!";
    }

}
