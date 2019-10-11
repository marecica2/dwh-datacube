package org.bmsource.dwh.common.repository;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProjectRepository {

    public List<String> getProjects(String tenant) {
        return Arrays.asList("1");
    }
}
