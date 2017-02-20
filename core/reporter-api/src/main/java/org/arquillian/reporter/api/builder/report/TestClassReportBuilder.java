package org.arquillian.reporter.api.builder.report;

import org.arquillian.reporter.api.model.report.TestClassReport;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public interface TestClassReportBuilder extends ReportBuilder<TestClassReportBuilder, TestClassReport> {

    TestClassReportBuilder stop();
}
