/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.nikita.sakharin.conwaysgameoflife;

import java.util.Scanner;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@Configuration
@PropertySource("classpath:first-generation.properties")
public class ApplicationConfig {
    private static final Scanner scanner = new Scanner(System.in);

    @Bean
    @Profile("!test")
    public Generation firstGeneration(@Value("${cells}") final String[] cells) {
        return new Generation(Stream.of(cells).map(Cell::parse));
    }

    @Bean("runnable")
    @Profile("auto")
    public Runnable auto() {
        return () -> System.out.println("Next iteration starts...");
    }

    @Bean("runnable")
    @Profile("manual")
    public Runnable manual() {
        return () -> {
            System.out.print("Press Enter to continue...");
            scanner.nextLine();
        };
    }
}
