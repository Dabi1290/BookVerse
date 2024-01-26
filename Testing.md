# Testing Configuration

## Unit testing

## Integration testing

## Functional testing

1. You must change the credentials within [context.xml](src%2Fmain%2Fwebapp%2FMETA-INF%2Fcontext.xml) to those of a user with permissions to recreate the database
2. You must execute the test with [selenium ide](https://chromewebstore.google.com/detail/selenium-ide/mooikfkahbdckldjjndioackbalphokd) and [Chrome Browser](https://www.google.com/intl/it/chrome/)
3. In Chrome you must enable permission for file uploading. Follow this [thread](https://stackoverflow.com/questions/50335595/selenium-ide-upload-file-into-field)

### Avanzamento Proposta

1. You must run [createDb.sql](db%2FcreateDb.sql) and [fillDb.sql](db%2FfillDb.sql) first of execute this test suite

### After functional testing

1. You must run [createDb.sql](db%2FcreateDb.sql) and [fillDb.sql](db%2FfillDb.sql) for put the db into normal state