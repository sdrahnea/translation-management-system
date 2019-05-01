package com.tms.controller;

import com.tms.controller.entity.Controller;
import com.tms.model.entity.Client;
import com.tms.model.entity.Currency;
import com.tms.model.entity.Person;
import com.tms.model.entity.Project;
import com.tms.model.entity.Translator;
import com.tms.model.entity.dao.ClientDao;
import com.tms.model.entity.dao.CurrencyDao;
import com.tms.model.entity.dao.PersonDao;
import com.tms.model.entity.dao.TranslatorDao;
import com.tms.model.entity.data.CustumResult;
import com.tms.util.CompareUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author sdrahnea
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class StatisticsController extends Controller<Project> implements Serializable, ISearcher {

    @Autowired
    private CurrencyDao currencyDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private TranslatorDao translatorDao;
    @Autowired
    private PersonDao personDao;

    private List<Currency> currencyList = new LinkedList<>();
    private List<Client> clientList = new LinkedList<>();
    private List<Translator> translatorList = new LinkedList<>();
    private List<Person> managerList = new LinkedList<>();

    private Date filterStartDate;
    private Date filterEndDate;
    private Person filterManager;
    private Client filterClient;
    private Translator filterTranslator;
    private Currency filterCurrency;

    private List<CustumResult> incomes = new LinkedList<>();
    private List<CustumResult> paidToTranslators = new LinkedList<>();
    private List<CustumResult> clientPaids = new LinkedList<>();
    private List<CustumResult> losss = new LinkedList<>();

    @PostConstruct
    @Override
    public void init() {
        try {
            this.translatorList = translatorDao.findAll();
            this.clientList = clientDao.findAll();
            this.managerList = personDao.findAllManagers();
            this.currencyList = currencyDao.findAll();

            refreshEntityList();
            refreshPayments();
        } catch (Exception ex) {

        }
    }

    @Override
    public void filter() {
        refreshEntityList();
        List<Project> result = new LinkedList<>();
        for (Project p : getEntityList()) {
            if (CompareUtil.comparasion(p.getAssignedManager() == null ? null : p.getAssignedManager().getName(), filterManager == null ? null : filterManager.getName())
                    && CompareUtil.comparasion(p.getClient() == null ? null : p.getClient().getName(), filterClient == null ? null : filterClient.getName())
                    && CompareUtil.comparasion(p.getCurrency() == null ? null : p.getCurrency().getName(), filterCurrency == null ? null : filterCurrency.getName())
                    && CompareUtil.comparasion(p.getTranslator() == null ? null : p.getTranslator().getName(), filterTranslator == null ? null : filterTranslator.getName())
                    && CompareUtil.comparasion(p.getInsertDate(), filterStartDate, -111)
                    && CompareUtil.comparasion(p.getInsertDate(), filterEndDate, 111)) {
                result.add(p);
            }
        }
        setEntityList(result);
        refreshPayments();
    }

    @Override
    public void clearFilters() {
        this.filterClient = null;
        this.filterCurrency = null;
        this.filterEndDate = null;
        this.filterManager = null;
        this.filterStartDate = null;
        this.filterTranslator = null;
        refreshEntityList();
        refreshPayments();
    }

    private void refreshPayments() {
        incomes.clear();
        clientPaids.clear();
        paidToTranslators.clear();
        losss.clear();
        for (Currency c : currencyList) {
            long incomeAmount = 0;
            long clientAmoun = 0;
            long translatorAmoun = 0;
            long lossAmoun = 0;
            for (Project p : getEntityList()) {
                if (p.getCurrency() != null) {
                    if (p.getCurrency().getId() == c.getId()) {
                        incomeAmount += (p.getClientActualPayment().longValue() - p.getTranslatorActualPayment().longValue());
                        clientAmoun += (p.getClientExpectedPayment().longValue() - p.getClientActualPayment().longValue());
                        translatorAmoun += (p.getTranslatorActualPayment().longValue());
                        lossAmoun += (p.getClientExpectedPayment().longValue() - p.getClientActualPayment().longValue());
                    }
                }
            }
            incomes.add(new CustumResult(c.getName(), new BigDecimal(incomeAmount)));
            clientPaids.add(new CustumResult(c.getName(), new BigDecimal(clientAmoun)));
            paidToTranslators.add(new CustumResult(c.getName(), new BigDecimal(translatorAmoun)));
            losss.add(new CustumResult(c.getName(), new BigDecimal(lossAmoun)));
        }
    }

    public Date getFilterStartDate() {
        return filterStartDate;
    }

    public void setFilterStartDate(Date filterStartDate) {
        this.filterStartDate = filterStartDate;
    }

    public Date getFilterEndDate() {
        return filterEndDate;
    }

    public void setFilterEndDate(Date filterEndDate) {
        this.filterEndDate = filterEndDate;
    }

    public Person getFilterManager() {
        return filterManager;
    }

    public void setFilterManager(Person filterManager) {
        this.filterManager = filterManager;
    }

    public Client getFilterClient() {
        return filterClient;
    }

    public void setFilterClient(Client filterClient) {
        this.filterClient = filterClient;
    }

    public Translator getFilterTranslator() {
        return filterTranslator;
    }

    public void setFilterTranslator(Translator filterTranslator) {
        this.filterTranslator = filterTranslator;
    }

    public Currency getFilterCurrency() {
        return filterCurrency;
    }

    public void setFilterCurrency(Currency filterCurrency) {
        this.filterCurrency = filterCurrency;
    }

    public List<CustumResult> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<CustumResult> incomes) {
        this.incomes = incomes;
    }

    public List<CustumResult> getPaidToTranslators() {
        return paidToTranslators;
    }

    public void setPaidToTranslators(List<CustumResult> paidToTranslators) {
        this.paidToTranslators = paidToTranslators;
    }

    public List<CustumResult> getClientPaids() {
        return clientPaids;
    }

    public void setClientPaids(List<CustumResult> clientPaids) {
        this.clientPaids = clientPaids;
    }

    public List<CustumResult> getLosss() {
        return losss;
    }

    public void setLosss(List<CustumResult> losss) {
        this.losss = losss;
    }

    public List<Translator> getTranslatorList() {
        return translatorList;
    }

    public void setTranslatorList(List<Translator> translatorList) {
        this.translatorList = translatorList;
    }

    public List<Currency> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    public List<Person> getManagerList() {
        return managerList;
    }

    public void setManagerList(List<Person> managerList) {
        this.managerList = managerList;
    }

}
