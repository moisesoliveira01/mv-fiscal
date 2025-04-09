package com.mvfiscal.api.userservice.suite;

import com.mvfiscal.api.userservice.service.UserServiceTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({UserServiceTest.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTestSuite {
}
