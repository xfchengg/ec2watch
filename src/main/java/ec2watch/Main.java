/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package ec2watch;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Main {

  private static Options options = null;

  public static void main(String[] args) throws Exception {

    options = new Options();

    Option intervalOption = new Option("t", true, "Interval in seconds");
    options.addOption(intervalOption);

    Option regionOption = new Option("r", true, "AWS Region");
    options.addOption(regionOption);

    Option helpOption = new Option("h", false, "Show this help");
    options.addOption(helpOption);

    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = null;
    
    String instanceId = null;
    
    try {
      cmd = parser.parse(options, args);
      if(args.length >= 1) {
        instanceId = args[args.length - 1];
        if(!instanceId.startsWith("i-")) {
          instanceId = null;
        }
      }
      if(instanceId == null) {
        throw new RuntimeException("Missing INSTANCE ID");
      }
    } catch (Exception e) {

      System.err.println(e.getMessage());
      printHelp();
      System.exit(1);
    }
    
    if(cmd.hasOption("h")) {
      printHelp();
      System.exit(1);
    }

    String region = cmd.getOptionValue("r");
    String interval = cmd.getOptionValue("t");

    EC2Watch ec2watch = new EC2Watch(instanceId, interval, region);
    ec2watch.run();

  }

  private static void printHelp() {
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.printHelp("java -jar ec2watch.jar [<options>] INSTANCE_ID", options);
  }
}
