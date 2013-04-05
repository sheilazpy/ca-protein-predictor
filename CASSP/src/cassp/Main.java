/**
*   Main.java
*
*   Copyright (c) 2013 Vladimir Brigant
*   This software is distributed under the terms of the GNU General Public License.
*/

package cassp;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.Properties;

import cassp.config.*;



/**
 * Main class
 */
public class Main {

    static Logger logger = Logger.getLogger(Main.class);

    // configuration paths
    static String confPath = "./src/cassp/config/test.config";
    static String logPath = "./src/cassp/config/log.config";

    public static void main(String[] args) {

        // logger configuration
        PropertyConfigurator.configure(Main.logPath);

        // simulator configuration
        SimConfig config = new SimConfig(Main.confPath);
        config.toFile("hello.config");

        // API demonstration
        CASSP simulator = new CASSP(config);
        simulator.train();
        //simulator.predict("AZTKKAZZZZKKKTKC");
        //simulator.loadRule();
        //simulator.testPsipred();
        System.out.println("Testing accuracy: " + simulator.test());
        //simulator.test();

        simulator.createEvolutionImage("evolution.png");
        simulator.createAccClassesImage("accuracy.png");
        simulator.createReliabImage("reliability.png");

        //double cv_acc = simulator.crossValidate(10);
    }
}
