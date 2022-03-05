package ru.consulting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.consulting.dto.PositionDto;
import ru.consulting.entitity.Position;
import ru.consulting.repositories.PositionRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PositionService {
    private PositionRepo positionRepo;

    @Autowired
    public PositionService(PositionRepo positionRepo) {
        this.positionRepo = positionRepo;
    }

    public void saveNewOrUpdate(PositionDto positionDto) {
        if (positionDto.getId() == null) {
            positionRepo.save(convertToPosition(positionDto));
        } else {
            Optional<Position> positionOptional = positionRepo.findById(positionDto.getId());
            if (positionOptional.isEmpty()) {
                positionRepo.save(convertToPosition(positionDto));
            } else {
                Position position = positionOptional.get();
                if (positionDto.getTitle() != null) {
                    position.setTitle(positionDto.getTitle());
                }
                if (positionDto.getJobFunction() != null) {
                    position.setJobFunction(positionDto.getJobFunction());
                }
                positionRepo.save(position);
            }
        }
    }

    public void removes(String[] titles) {
        for (String title : titles) {
            Optional<Position> positionOptional = positionRepo
                    .findByTitleContainingIgnoreCase(title);
            positionOptional.ifPresent(position -> positionRepo.delete(position));
        }
    }

    public Iterable<PositionDto> getAll(Boolean idAscending, Boolean idDescending, Boolean title) {
        List<PositionDto> positionDtos = new ArrayList<>();
        if (idAscending != null) {
            positionRepo.getOrderById().forEach(position -> positionDtos.add(convertToPositionDto(position)));
        } else if (idDescending != null) {
            positionRepo.getOrderByIdDesc().forEach(position -> positionDtos.add(convertToPositionDto(position)));
        } else if (title != null) {
            positionRepo.getOrderByTitle().forEach(position -> positionDtos.add(convertToPositionDto(position)));
        } else {
            positionRepo.findAll().iterator().forEachRemaining(position -> positionDtos.add(convertToPositionDto(position)));
        }
        return positionDtos;
    }

    public Position convertToPosition(PositionDto positionDto) {
        return new Position().setTitle(positionDto.getTitle()).setJobFunction(positionDto.getJobFunction());
    }

    public PositionDto convertToPositionDto(Position position) {
        return new PositionDto(position.getId(), position.getTitle(), position.getJobFunction());
    }


}
