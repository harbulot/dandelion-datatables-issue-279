/*
 * [The "BSD licence"]
 * Copyright (c) 2015 Bruno Harbulot
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder may be used to endorse or 
 * promote products derived from this software without specific prior 
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.harbulot.dandelion_datatables_issue_279.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

@Controller
@RequestMapping(value = "/")
public class MainController {
    private final MappingJacksonJsonView apiView1 = new MappingJacksonJsonView();

    public MainController() {
        apiView1.setExtractValueFromSingleKeyModel(true);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getNoSlash(Model model) {
        return "redirect:/page1";
    }

    @RequestMapping(value = "/page1", method = RequestMethod.GET)
    public String getPage1(Model model) {
        return "page1";
    }

    @RequestMapping(value = "/page2", method = RequestMethod.GET)
    public String getPage2(Model model) {
        return "page2";
    }

    @RequestMapping(value = "/dummydata1", method = RequestMethod.GET)
    public @ResponseBody View getDummyData1(Model model) {
        List<Map<String, Integer>> dummyData1 = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("col_a", 10);
        m.put("col_b", 1);
        dummyData1.add(m);
        m = new HashMap<String, Integer>();
        m.put("col_a", 20);
        m.put("col_b", 3);
        dummyData1.add(m);
        m = new HashMap<String, Integer>();
        m.put("col_a", 50);
        m.put("col_b", 6);
        dummyData1.add(m);

        model.addAttribute(dummyData1);
        return apiView1;
    }

    @RequestMapping(value = "/dummydata2", method = RequestMethod.GET)
    public @ResponseBody View getDummyData2(Model model) {
        List<Map<String, Integer>> dummyData1 = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("col_a", 1);
        m.put("col_b", 1);
        dummyData1.add(m);
        m = new HashMap<String, Integer>();
        m.put("col_a", 2);
        m.put("col_b", 3);
        dummyData1.add(m);
        m = new HashMap<String, Integer>();
        m.put("col_a", 5);
        m.put("col_b", 6);
        dummyData1.add(m);

        model.addAttribute(dummyData1);
        return apiView1;
    }
}
