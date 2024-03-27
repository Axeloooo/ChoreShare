package com.choresync.task.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.choresync.task.entity.Frequency;
import com.choresync.task.entity.Status;
import com.choresync.task.entity.Tag;
import com.choresync.task.entity.Task;
import com.choresync.task.exception.TaskInternalCommunicationException;
import com.choresync.task.exception.TaskInvalidBodyException;
import com.choresync.task.exception.TaskInvalidParamException;
import com.choresync.task.exception.TaskNotFoundException;
import com.choresync.task.exception.TaskUnforbiddenActionException;
import com.choresync.task.external.exception.HouseholdNotFoundException;
import com.choresync.task.external.exception.UserNotFoundException;
import com.choresync.task.external.response.HouseholdResponse;
import com.choresync.task.external.response.UserResponse;
import com.choresync.task.model.TaskEditMetadataRequest;
import com.choresync.task.model.TaskEditStatusRequest;
import com.choresync.task.model.TaskRequest;
import com.choresync.task.model.TaskResponse;
import com.choresync.task.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TaskServiceImpl implements TaskService {
  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private RestTemplate restTemplate;

  /*
   * Extracts the error message from a RestClientException
   * 
   * @param e
   * 
   * @return String
   */
  @Override
  public String extractErrorMessage(RestClientException e) {
    String rawMessage = e.getMessage();

    try {
      String jsonSubstring = rawMessage.substring(rawMessage.indexOf("{"), rawMessage.lastIndexOf("}") + 1);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(jsonSubstring);

      if (rootNode.has("message")) {
        return rootNode.get("message").asText();
      }
    } catch (JsonProcessingException ex) {
      System.out.println("Error parsing JSON from exception message: " + ex.getMessage());
    } catch (StringIndexOutOfBoundsException ex) {
      System.out.println("Error extracting JSON substring from exception message: " + ex.getMessage());
    }
    return rawMessage;
  }

  /*
   * Creates a new task.
   * 
   * @param taskRequest
   * 
   * @return TaskResponse
   * 
   * @throws TaskInvalidBodyException
   * 
   * @throws TaskInternalCommunicationException
   * 
   * @throws HouseholdNotFoundException
   * 
   * @throws UserNotFoundException
   */
  @Override
  public TaskResponse createTask(TaskRequest taskRequest) {
    if (taskRequest.getTitle().isBlank() || taskRequest.getTitle() == null || taskRequest.getHouseholdId().isBlank()
        || taskRequest.getHouseholdId() == null
        || taskRequest.getFrequency().isBlank()
        || taskRequest.getFrequency() == null || taskRequest.getTag().isBlank() || taskRequest.getTag() == null
        || taskRequest.getUserId().isBlank() || taskRequest.getUserId() == null) {
      throw new TaskInvalidBodyException("Invalid request body");
    }

    try {
      HouseholdResponse householdResponse = restTemplate.getForObject(
          "http://household-service/api/v1/household/" + taskRequest.getHouseholdId(),
          HouseholdResponse.class);

      if (householdResponse == null) {
        throw new HouseholdNotFoundException("Could not find a household");
      }
    } catch (RestClientException e) {
      throw new TaskInternalCommunicationException(extractErrorMessage(e));
    }

    try {
      UserResponse userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/" + taskRequest.getUserId(), UserResponse.class);

      if (userResponse == null) {
        throw new UserNotFoundException("Could not find a user");
      }
    } catch (RestClientException e) {
      throw new TaskInternalCommunicationException(extractErrorMessage(e));
    }

    Task task = Task.builder()
        .title(taskRequest.getTitle())
        .householdId(taskRequest.getHouseholdId())
        .description(taskRequest.getDescription())
        .status(Status.PENDING)
        .frequency(Frequency.valueOf(taskRequest.getFrequency()))
        .tag(Tag.valueOf(taskRequest.getTag()))
        .userId(null)
        .build();

    Task newTask = taskRepository.save(task);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(newTask.getId())
        .title(newTask.getTitle())
        .householdId(newTask.getHouseholdId())
        .description(newTask.getDescription())
        .status(newTask.getStatus().name())
        .frequency(newTask.getFrequency().name())
        .tag(newTask.getTag().name())
        .userId(null)
        .createdAt(newTask.getCreatedAt())
        .updatedAt(newTask.getUpdatedAt())
        .build();

    return taskResponse;
  }

  /*
   * Retrieves a task by its ID.
   * 
   * @param id
   * 
   * @return TaskResponse
   * 
   * @throws TaskInvalidParamException
   * 
   * @throws TaskNotFoundException
   */
  @Override
  public TaskResponse getTaskById(String id) {
    if (id.isBlank() || id == null) {
      throw new TaskInvalidParamException("Invalid request parameter");
    }
    Task task = taskRepository.findById(id).orElseThrow(() -> {
      throw new TaskNotFoundException("Task not found");
    });

    TaskResponse taskResponse = TaskResponse.builder()
        .id(task.getId())
        .title(task.getTitle())
        .householdId(task.getHouseholdId())
        .description(task.getDescription())
        .status(task.getStatus().name())
        .frequency(task.getFrequency().name())
        .tag(task.getTag().name())
        .userId(task.getUserId())
        .createdAt(task.getCreatedAt())
        .updatedAt(task.getUpdatedAt())
        .build();

    return taskResponse;
  }

  /*
   * Retrieves all tasks by a user ID.
   * 
   * @param userId
   * 
   * @return List<TaskResponse>
   * 
   * @throws TaskInvalidParamException
   * 
   * @throws TaskInternalCommunicationException
   * 
   * @throws UserNotFoundException
   */
  @Override
  public List<TaskResponse> getAllTasksByUserId(String userId) {
    if (userId.isBlank() || userId == null) {
      throw new TaskInvalidParamException("Invalid request parameter");
    }

    try {
      UserResponse userResponse = restTemplate.getForObject(
          "http://user-service/api/v1/user/" + userId,
          UserResponse.class);

      if (userResponse == null) {
        throw new UserNotFoundException("Could not find a user");
      }
    } catch (RestClientException e) {
      throw new TaskInternalCommunicationException(extractErrorMessage(e));
    }

    List<Task> tasks = taskRepository.findByUserId(userId);

    List<TaskResponse> taskResponses = new ArrayList<>();

    for (Task task : tasks) {
      TaskResponse taskResponse = TaskResponse.builder()
          .id(task.getId())
          .title(task.getTitle())
          .householdId(task.getHouseholdId())
          .description(task.getDescription())
          .status(task.getStatus().name())
          .frequency(task.getFrequency().name())
          .tag(task.getTag().name())
          .userId(task.getUserId())
          .createdAt(task.getCreatedAt())
          .updatedAt(task.getUpdatedAt())
          .build();

      taskResponses.add(taskResponse);
    }

    return taskResponses;
  }

  /*
   * Retrieves all tasks by a household ID.
   * 
   * @param householdId
   * 
   * @return List<TaskResponse>
   * 
   * @throws TaskInvalidParamException
   * 
   * @throws TaskInternalCommunicationException
   * 
   * @throws HouseholdNotFoundException
   */
  @Override
  public List<TaskResponse> getAllTasksByHouseholdId(String householdId) {
    if (householdId.isBlank() || householdId == null) {
      throw new TaskInvalidParamException("Invalid request parameter");
    }

    try {
      HouseholdResponse householdResponse = restTemplate.getForObject(
          "http://household-service/api/v1/household/" + householdId,
          HouseholdResponse.class);

      if (householdResponse == null) {
        throw new HouseholdNotFoundException("Could not find a household");
      }
    } catch (RestClientException e) {
      throw new TaskInternalCommunicationException(extractErrorMessage(e));
    }

    List<Task> tasks = taskRepository.findByHouseholdId(householdId);

    List<TaskResponse> taskResponses = new ArrayList<>();

    for (Task task : tasks) {
      TaskResponse taskResponse = TaskResponse.builder()
          .id(task.getId())
          .title(task.getTitle())
          .householdId(task.getHouseholdId())
          .description(task.getDescription())
          .status(task.getStatus().name())
          .frequency(task.getFrequency().name())
          .tag(task.getTag().name())
          .userId(task.getUserId())
          .createdAt(task.getCreatedAt())
          .updatedAt(task.getUpdatedAt())
          .build();

      taskResponses.add(taskResponse);
    }

    return taskResponses;
  }

  /*
   * Updates a task by its ID.
   * 
   * @param id
   * 
   * @param taskRequest
   * 
   * @return TaskResponse
   * 
   * @throws TaskInvalidParamException
   * 
   * @throws TaskInvalidBodyException
   * 
   * @throws TaskNotFoundException
   */
  @Override
  public TaskResponse updateTask(String id, TaskEditMetadataRequest taskRequest) {
    if (id.isBlank() || id == null) {
      throw new TaskInvalidParamException("Invalid request parameter");
    }

    if (taskRequest.getTitle() == null || taskRequest.getTitle().isBlank()
        || taskRequest.getFrequency() == null
        || taskRequest.getFrequency().isBlank() || taskRequest.getTag() == null || taskRequest.getTag().isBlank()) {
      throw new TaskInvalidBodyException("Invalid request body");
    }

    Task task = taskRepository.findById(id).orElseThrow(() -> {
      throw new TaskNotFoundException("Task not found");
    });

    task.setTitle(taskRequest.getTitle());
    task.setDescription(taskRequest.getDescription());
    task.setFrequency(Frequency.valueOf(taskRequest.getFrequency()));
    task.setTag(Tag.valueOf(taskRequest.getTag()));

    Task updatedTask = taskRepository.save(task);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(updatedTask.getId())
        .title(updatedTask.getTitle())
        .householdId(updatedTask.getHouseholdId())
        .description(updatedTask.getDescription())
        .status(updatedTask.getStatus().name())
        .frequency(updatedTask.getFrequency().name())
        .tag(updatedTask.getTag().name())
        .userId(updatedTask.getUserId())
        .createdAt(updatedTask.getCreatedAt())
        .updatedAt(updatedTask.getUpdatedAt())
        .build();

    return taskResponse;
  }

  /*
   * Updates a task status by its ID.
   * 
   * @param id
   * 
   * @param taskRequest
   * 
   * @return TaskResponse
   * 
   * @throws TaskInvalidParamException
   * 
   * @throws TaskInvalidBodyException
   * 
   * @throws TaskNotFoundException
   * 
   * @throws TaskUnforbiddenActionException
   */
  @Override
  public TaskResponse updateTaskStatus(String id, TaskEditStatusRequest taskRequest) {
    if (id.isBlank() || id == null) {
      throw new TaskInvalidParamException("Invalid request parameter");
    }

    if (taskRequest.getStatus().isBlank() || taskRequest.getStatus() == null || taskRequest.getUserId().isBlank()
        || taskRequest.getUserId() == null) {
      throw new TaskInvalidBodyException("Invalid request body");
    }

    Task task = taskRepository.findById(id).orElseThrow(() -> {
      throw new TaskNotFoundException("Task not found");
    });

    if (!task.getUserId().equals(taskRequest.getUserId())) {
      throw new TaskUnforbiddenActionException("User not allowed to perform this action");
    }

    task.setStatus(Status.valueOf(taskRequest.getStatus()));

    Task updatedTask = taskRepository.save(task);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(updatedTask.getId())
        .title(updatedTask.getTitle())
        .householdId(updatedTask.getHouseholdId())
        .description(updatedTask.getDescription())
        .status(updatedTask.getStatus().name())
        .frequency(updatedTask.getFrequency().name())
        .tag(updatedTask.getTag().name())
        .userId(updatedTask.getUserId())
        .createdAt(updatedTask.getCreatedAt())
        .updatedAt(updatedTask.getUpdatedAt())
        .build();

    return taskResponse;
  }

  /*
   * Deletes a task by its ID.
   * 
   * @param id
   * 
   * @throws TaskInvalidParamException
   * 
   * @throws TaskNotFoundException
   */
  @Override
  public void deleteTask(String id) {
    if (id.isBlank() || id == null) {
      throw new TaskInvalidParamException("Invalid request parameter");
    }

    if (!taskRepository.existsById(id)) {
      throw new TaskNotFoundException("Task not found");
    }

    taskRepository.deleteById(id);
  }

  /*
   * Unassigns a task from a user.
   * 
   * @param id
   * 
   * @param userId
   * 
   * @return TaskResponse
   * 
   * @throws TaskInvalidParamException
   * 
   * @throws TaskNotFoundException
   * 
   * @throws TaskUnforbiddenActionException
   */
  @Override
  public TaskResponse unassignTask(String id, String userId) {
    if (id.isBlank() || id == null || userId.isBlank() || userId == null) {
      throw new TaskInvalidParamException("Invalid request parameter");
    }

    Task task = taskRepository.findById(id).orElseThrow(() -> {
      throw new TaskNotFoundException("Task not found");
    });

    if (!task.getUserId().equals(userId)) {
      throw new TaskUnforbiddenActionException("User not allowed to perform this action");
    }

    task.setUserId(null);
    task.setStatus(Status.PENDING);

    Task updatedTask = taskRepository.save(task);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(updatedTask.getId())
        .title(updatedTask.getTitle())
        .householdId(updatedTask.getHouseholdId())
        .description(updatedTask.getDescription())
        .status(updatedTask.getStatus().name())
        .frequency(updatedTask.getFrequency().name())
        .tag(updatedTask.getTag().name())
        .userId(updatedTask.getUserId())
        .createdAt(updatedTask.getCreatedAt())
        .updatedAt(updatedTask.getUpdatedAt())
        .build();

    return taskResponse;
  }

  /*
   * Assigns a task to a user.
   * 
   * @param id
   * 
   * @param userId
   * 
   * @return TaskResponse
   * 
   * @throws TaskInvalidParamException
   * 
   * @throws TaskNotFoundException
   * 
   * @throws TaskUnforbiddenActionException
   */
  @Override
  public TaskResponse assignTask(String id, String userId) {
    if (userId.isBlank() || userId == null || id.isBlank() || id == null) {
      throw new TaskInvalidParamException("Invalid request parameter");
    }

    Task task = taskRepository.findById(id).orElseThrow(() -> {
      throw new TaskNotFoundException("Task not found");
    });

    if (task.getUserId() != null) {
      throw new TaskUnforbiddenActionException("Task already assigned");
    }

    task.setUserId(userId);

    Task updatedTask = taskRepository.save(task);

    TaskResponse taskResponse = TaskResponse.builder()
        .id(updatedTask.getId())
        .title(updatedTask.getTitle())
        .householdId(updatedTask.getHouseholdId())
        .description(updatedTask.getDescription())
        .status(updatedTask.getStatus().name())
        .frequency(updatedTask.getFrequency().name())
        .tag(updatedTask.getTag().name())
        .userId(updatedTask.getUserId())
        .createdAt(updatedTask.getCreatedAt())
        .updatedAt(updatedTask.getUpdatedAt())
        .build();

    return taskResponse;
  }
}