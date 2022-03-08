package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.PositionDto;
import ru.consulting.service.PositionService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("position")
public class PositionController {
    private PositionService positionService;

    @Autowired
    public void setPositionService(PositionService positionService) {
        this.positionService = positionService;
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveOrUpdate(@RequestBody @Valid PositionDto positionDto) {
        return positionService.saveNewOrUpdate(positionDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void deletesByTitles(@RequestBody @NotEmpty String[] titles) {
        positionService.removes(titles);
    }

    @RequestMapping("{title}")
    public ResponseEntity<Iterable<PositionDto>> showAllBySorting(@RequestParam(required = false) Boolean idAscending,
                                                                  @RequestParam(required = false) Boolean idDescending,
                                                                  @PathVariable(required = false) boolean title) {

        return ResponseEntity.ok(positionService.getAll(idAscending, idDescending, title));
    }

}
