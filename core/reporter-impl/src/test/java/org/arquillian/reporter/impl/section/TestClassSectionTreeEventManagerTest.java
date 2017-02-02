package org.arquillian.reporter.impl.section;

import java.util.List;
import java.util.Map;

import org.arquillian.reporter.api.event.SectionEvent;
import org.arquillian.reporter.api.event.TestSuiteSection;
import org.arquillian.reporter.api.model.report.ConfigurationReport;
import org.arquillian.reporter.api.model.report.TestClassReport;
import org.arquillian.reporter.api.model.report.TestSuiteReport;
import org.arquillian.reporter.impl.ExecutionReport;
import org.arquillian.reporter.impl.ExecutionSection;
import org.junit.Test;

import static org.arquillian.reporter.impl.asserts.SectionTreeAssert.assertThatSectionTree;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.EXPECTED_NUMBER_OF_SECTIONS;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.feedWithTestClassConfigurationSections;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.feedWithTestClassSections;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.feedWithTestSuiteSections;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.getSubsectionsOfSomeSection;
import static org.arquillian.reporter.impl.utils.SectionTreeEventManagerUtils.verifyAllSectionsAreProcessed;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class TestClassSectionTreeEventManagerTest {

    @Test
    public void testAddingTestClassSectionsWithReportsIntoExistingTreeUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(
            feedWithTestClassSections(executionReport, getSubsectionsOfSomeSection(ExecutionSection.class, sections)));

        int parentCount = 1 + EXPECTED_NUMBER_OF_SECTIONS;
        int treeNodesCount = parentCount + EXPECTED_NUMBER_OF_SECTIONS;
        assertThat(sections).hasSize(parentCount);
        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport()
            .wholeExecutionReportTreeConsistOfGeneratedReports(TestSuiteReport.class, TestClassReport.class);
        verifyAllSectionsAreProcessed(sections);
    }

    @Test
    public void testAddTestClassConfigurationSectionsWithReportsToExistingTreeUsingEventManager() {
        ExecutionReport executionReport = new ExecutionReport();
        Map<SectionEvent, List<? extends SectionEvent>> sections = feedWithTestSuiteSections(executionReport);
        sections.putAll(
            feedWithTestClassSections(executionReport, getSubsectionsOfSomeSection(ExecutionSection.class, sections)));
        sections.putAll(
            feedWithTestClassConfigurationSections(executionReport,
                                                   getSubsectionsOfSomeSection(TestSuiteSection.class, sections)));

        int parentCount = 1 + EXPECTED_NUMBER_OF_SECTIONS * 2;
        int treeNodesCount = (int) (parentCount + Math.pow(EXPECTED_NUMBER_OF_SECTIONS, 2));
        assertThat(sections).hasSize(parentCount);
        assertThatSectionTree(executionReport.getSectionTree())
            .wholeTreeConsistOfCouplesMathing(sections)
            .wholeTreeHasNumberOfTreeNodes(treeNodesCount)
            .associatedReport()
            .wholeExecutionReportTreeConsistOfGeneratedReports(TestSuiteReport.class,
                                                               TestClassReport.class,
                                                               ConfigurationReport.class);
        verifyAllSectionsAreProcessed(sections);
    }
}
