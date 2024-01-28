# Testing Configuration

## Unit testing

1. You must insert your credentials in the file [credentials.xml](src%2Ftest%2Fcredentials.xml)
2. Then you can run testing located in the directory [unitTesting](src%2Ftest%2Fjava%2FunitTesting)

## Integration testing

1. You must insert you credentials in the file [credentials.xml](src%2Ftest%2Fcredentials.xml)
2. Then you can run testing located in the directory [storageSubSystem](src%2Ftest%2Fjava%2FintegrationTesting%2FstorageSubSystem)

## Functional testing

1. You must change the credentials within [context.xml](src%2Fmain%2Fwebapp%2FMETA-INF%2Fcontext.xml) to those of a user with permissions to recreate the database
2. You must execute the test with [selenium ide](https://chromewebstore.google.com/detail/selenium-ide/mooikfkahbdckldjjndioackbalphokd) and [Chrome Browser](https://www.google.com/intl/it/chrome/)
3. In Chrome you must enable permission for file uploading. Follow this [thread](https://stackoverflow.com/questions/50335595/selenium-ide-upload-file-into-field)

### Avanzamento Proposta

1. You must run [createDb.sql](db%2FcreateDb.sql) and [fillDb.sql](db%2FfillDb.sql) first of execute this test suite

### After functional testing

1. You must run [createDb.sql](db%2FcreateDb.sql) and [fillDb.sql](db%2FfillDb.sql) for put the db into normal state