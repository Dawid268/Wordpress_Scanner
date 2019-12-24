package com.company.working;

import j2html.TagCreator;
import j2html.tags.DomContent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static j2html.TagCreator.a;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.body;
import static j2html.TagCreator.br;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h1;
import static j2html.TagCreator.head;
import static j2html.TagCreator.li;
import static j2html.TagCreator.link;
import static j2html.TagCreator.main;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.nav;
import static j2html.TagCreator.p;
import static j2html.TagCreator.title;
import static j2html.TagCreator.ul;

public class Service
{
    Logger log = Logger.getLogger(Service.class.getName());
    private String path;
    private String url;
    private int portStart;
    private int portEnd;
    
    public Service(String url, int portStart, int portEnd, String path)
    {
        this.url = url;
        this.portStart = portStart;
        this.portEnd = portEnd;
        this.path = path;
    }
    
    public void run()
    {
        try
        {
            generateRaport();
            
        }
        catch(Exception e)
        {
            log.info("Wystąpił problem wygeneruje raport z tego co udało mi się ustalić" + e);
        }
        
    }
    
    private void generateRaport()
    {
        
        createFileFromHtml(createMenuHtml());
        
    }
    
    private List<String> createMenuHtml()
    {
        DigDNS digDNS = new DigDNS(url);
        PortsScanner portsScanner = new PortsScanner(url);
        JsoupSiteParser jsoupSiteParser = new JsoupSiteParser(url);
        List<String> htmlList = new ArrayList<>();
        List<DomContent> optionsList = new ArrayList<>();
        optionsList.add(div(attrs(".container"), br(), p(each(digDNS.Dig(), d -> p(d)))));
        optionsList.add(div(attrs(".container"), br(), each(portsScanner.getWhoIsResult(), d -> p(d))));
        optionsList.add(div(attrs(".container"), br(), each(digDNS.traceRoute(), d -> p(d))));
        optionsList.add(div(attrs(".container"), br(), each(portsScanner.portScanner(portStart, portEnd), d -> p(a(String.valueOf(d))))));
        optionsList.add(div(attrs(".container"), br(), ul(each(jsoupSiteParser.getImportsList(), d -> li(a(d).withHref(d)).withClass("list-group-item"))).withClass("list-group")));
        optionsList.add(div(attrs(".container"), br(), ul(each(jsoupSiteParser.getLinkList(), d -> li(a(d).withHref(d)).withClass("list-group-item"))).withClass("list-group")));
        optionsList.add(div(attrs(".container"), br(), ul(each(jsoupSiteParser.getMediaList(), d -> li(a(d).withHref(d)).withClass("list-group-item"))).withClass("list-group")));
        optionsList.add(div(attrs(".container"), br(), ul(each(jsoupSiteParser.getMetaList(), d -> li(a(d).withHref(d)).withClass("list-group-item"))).withClass("list-group")));
        optionsList.add(div(attrs(".container"), br(), ul(each(jsoupSiteParser.getCveModelList(), d -> li(String.valueOf(d)).withClass("list-group-item")))));
        optionsList.add(div(attrs(".container"), br(), ul(each(jsoupSiteParser.getAdminUsernames(), d -> li(a(String.valueOf(d))).withClass("list-group-item"))).withClass("list-group")));
        optionsList.add(div(attrs(".container"), br(), ul(each(jsoupSiteParser.getUsersNamesListFromAuthorApi(), d -> li(a(String.valueOf(d))).withClass("list-group-item"))).withClass("list-group")));
        
        for(DomContent html : optionsList)
        {
            try
            {
                htmlList.add(TagCreator.html(
                    head(
                        meta().attr("charset=UTF-8"),
                        title(url),
                        link().withRel("stylesheet")
                              .withHref("https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css")
                        ),
                    body(
                        main(
                            attrs(".container"),
                            h1(attrs("#h1"), url),
                            nav()
                                .withClass("navbar navbar-expand-md navbar-dark bg-dark mb-3")
                                .with(div(attrs("#navbarNavAltMarkup.collapse navbar-collapse"))
                                          .with(div()
                                                    .withClass("navbar-nav")
                                                    .with(
                                                        a("Dig")
                                                            .withClass("nav-item nav-link")
                                                            .withHref(path + "Dig.html"),
                                                        a("Whois")
                                                            .withClass("nav-item nav-link")
                                                            .withHref(path + "Whois.html"),
                                                        a("Traceroute")
                                                            .withClass("nav-item nav-link")
                                                            .withHref(path + "Traceroute.html"),
                                                        a("Port Scanner")
                                                            .withClass("nav-item nav-link")
                                                            .withHref(path + "PortScanner.html"),
                                                        a("Imports Parser")
                                                            .withClass("nav-item nav-link")
                                                            .withHref(path + "Imports.html"),
                                                        a("Links Parser")
                                                            .withClass("nav-item nav-link")
                                                            .withHref(path + "Links.html"),
                                                        a("Media Parser")
                                                            .withClass("nav-item nav-link")
                                                            .withHref(path + "Media.html"),
                                                        a("Meta Parser")
                                                            .withClass("nav-item nav-link")
                                                            .withHref(path + "Meta.html"),
                                                        a("Plugins")
                                                            .withClass("nav-item nav-link")
                                                            .withHref(path + "Plugins.html"),
                                                        a("Admins")
                                                            .withClass("nav-item nav-link")
                                                            .withHref(path + "Admins.html"),
                                                        a("Users")
                                                            .withClass("nav-item nav-link")
                                                            .withHref(path + "Users.html")
                                                         )))
                            ), html
                        )
                                            ).render());
            }
            catch(Exception e)
            {
                log.info("Coś poszło nie tak " + e);
            }
        }
        
        this.createUsersList(jsoupSiteParser);
        return htmlList;
    }
    
    private File createUsersList(JsoupSiteParser jsoupSiteParser)
    {
        File file = new File(path + "users.txt");
        PrintWriter printWriter = null;
        try
        {
            printWriter = new PrintWriter(file);
        }
        catch(FileNotFoundException e)
        {
            log.info("Coś poszło nie tak " + e);
        }
        PrintWriter finalPrintWriter = printWriter;
        jsoupSiteParser.getUsersNamesListFromAuthorApi().forEach(value -> {
            finalPrintWriter.println(value.getUserName());
        });
        printWriter.close();
        return file;
    }
    
    private File createFileFromHtml(List<String> htmlList)
    {
        String[] sitesNames = {"Dig", "Whois", "Traceroute", "PortScanner", "Imports", "Links", "Media", "Meta", "Plugins", "Admins", "Users"};
        File file = null;
        for(int i = 0; i < sitesNames.length; i++)
        {
            file = new File(path + sitesNames[i] + ".html");
            PrintWriter printWriter = null;
            try
            {
                printWriter = new PrintWriter(file);
            }
            catch(FileNotFoundException e)
            {
                log.info("Coś poszło nie tak " + e);
            }
            printWriter.println(htmlList.get(i));
            printWriter.close();
        }
        return file;
    }
    
}
