package com.mvfiscal.api.taskservice.suite;

import com.mvfiscal.api.taskservice.service.TaskServiceTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({TaskServiceTest.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskServiceTestSuite {
}
