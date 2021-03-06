/*******************************************************************************
 * Copyright (c) 2006, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.persistence.jpa.internal.jpql;

/**
 * The list of messages used by {@link JPQLQueryProblem JPQLQueryProblems} when validating a
 * JPQL query.
 *
 * @version 2.3
 * @since 2.3
 * @author Pascal Filion
 */
@SuppressWarnings("nls")
public interface JPQLQueryProblemMessages {

	String AbsExpression_InvalidExpression = "ABS_EXPRESSION_INVALID_EXPRESSION";
	String AbsExpression_InvalidNumericExpression = "ABS_EXPRESSION_INVALID_NUMERIC_EXPRESSION";
	String AbsExpression_MissingExpression = "ABS_EXPRESSION_MISSING_EXPRESSION";
	String AbsExpression_MissingLeftParenthesis = "ABS_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String AbsExpression_MissingRightParenthesis = "ABS_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String AbstractFromClause_IdentificationVariableDeclarationEndsWithComma = "ABSTRACT_FROM_CLAUSE_IDENTIFICATION_VARIABLE_DECLARATION_ENDS_WITH_COMMA";
	String AbstractFromClause_IdentificationVariableDeclarationIsMissingComma = "ABSTRACT_FROM_CLAUSE_IDENTIFICATION_VARIABLE_DECLARATION_IS_MISSING_COMMA";
	String AbstractFromClause_MissingIdentificationVariableDeclaration = "ABSTRACT_FROM_CLAUSE_MISSING_IDENTIFICATION_VARIABLE_DECLARATION";
	String AbstractFromClause_WrongOrderOfIdentificationVariableDeclaration = "ABSTRACT_FROM_CLAUSE_WRONG_ORDER_OF_IDENTIFICATION_VARIABLE_DECLARATION";
	String AbstractPathExpression_CannotEndWithComma = "ABSTRACT_PATH_EXPRESSION_CANNOT_END_WITH_COMMA";
	String AbstractPathExpression_MissingIdentificationVariable = "ABSTRACT_PATH_EXPRESSION_MISSING_IDENTIFICATION_VARIABLE";
	String AbstractSchemaName_Invalid = "ABSTRACT_SCHEMA_NAME_INVALID";
	String AbstractSchemaName_NotResolvable = "ABSTRACT_SCHEMA_NAME_NOT_RESOLVABLE";
	String AbstractSelectClause_SelectExpressionEndsWithComma = "ABSTRACT_SELECT_CLAUSE_SELECT_EXPRESSION_ENDS_WITH_COMMA";
	String AbstractSelectClause_SelectExpressionIsMissingComma = "ABSTRACT_SELECT_CLAUSE_SELECT_EXPRESSION_IS_MISSING_COMMA";
	String AbstractSelectClause_SelectExpressionMissing = "ABSTRACT_SELECT_CLAUSE_SELECT_MISSING_EXPRESSION";
	String AbstractSelectStatement_FromClauseMissing = "ABSTRACT_SELECT_STATEMENT_FROM_CLAUSE_MSSING";
	String AdditionExpression_LeftExpression_WrongType = "ADDITION_EXPRESSION_LEFT_EXPRESSION_WRONG_TYPE";
	String AdditionExpression_RightExpression_WrongType = "ADDITION_EXPRESSION_RIGHT_EXPRESSION_WRONG_TYPE";
	String AllOrAnyExpression_All_ParentNotComparisonExpression = "ALL_OR_ANY_EXPRESSION_PARENT_NOT_COMPARISON_EXPRESSION";
	String AllOrAnyExpression_Any_ParentNotComparisonExpression = "ALL_OR_ANY_EXPRESSION_PARENT_NOT_COMPARISON_EXPRESSION";
	String AllOrAnyExpression_InvalidExpression = "ALL_OR_ANY_EXPRESSION_INVALID_EXPRESSION";
	String AllOrAnyExpression_MissingExpression = "ALL_OR_ANY_EXPRESSION_MISSING_EXPRESSION";
	String AllOrAnyExpression_MissingLeftParenthesis = "ALL_OR_ANY_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String AllOrAnyExpression_MissingRightParenthesis = "ALL_OR_ANY_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String AllOrAnyExpression_NotPartOfComparisonExpression = "ALL_OR_ANY_EXPRESSION_NOT_PART_OF_COMPARISON_EXPRESSION";
	String AllOrAnyExpression_Some_ParentNotComparisonExpression = "ALL_OR_ANY_EXPRESSION_PARENT_NOT_COMPARISON_EXPRESSION";
	String ArithmeticExpression_InvalidLeftExpression = "ARITHMETIC_EXPRESSION_INVALID_LEFT_EXPRESSION";
	String ArithmeticExpression_InvalidRightExpression = "ARITHMETIC_EXPRESSION_INVALID_RIGHT_EXPRESSION";
	String ArithmeticExpression_MissingLeftExpression = "ARITHMETIC_EXPRESSION_MISSING_LEFT_EXPRESSION";
	String ArithmeticExpression_MissingRightExpression = "ARITHMETIC_EXPRESSION_MISSING_RIGHT_EXPRESSION";
	String ArithmeticFactor_InvalidExpression = "ARITHMETIC_FACTOR_INVALID_EXPRESSION";
	String ArithmeticFactor_MissingExpression = "ARITHMETIC_FACTOR_MISSING_EXPRESSION";
	String AvgFunction_InvalidExpression = "AVG_FUNCTION_INVALID_EXPRESSION";
	String AvgFunction_InvalidNumericExpression = "AVG_FUNCTION_INVALID_NUMERIC_EXPRESSION";
	String AvgFunction_MissingExpression = "AVG_FUNCTION_MISSING_EXPRESSION";
	String AvgFunction_MissingLeftParenthesis = "AVG_FUNCTION_MISSING_LEFT_PARENTHESIS";
	String AvgFunction_MissingRightParenthesis = "AVG_FUNCTION_MISSING_RIGHT_PARENTHESIS";
	String BetweenExpression_MissingAnd = "BETWEEN_EXPRESSION_MISSING_AND";
	String BetweenExpression_MissingExpression = "BETWEEN_EXPRESSION_MISSING_EXPRESSION";
	String BetweenExpression_MissingLowerBoundExpression = "BETWEEN_EXPRESSION_MISSING_LOWER_BOUND_EXPRESSION";
	String BetweenExpression_MissingUpperBoundExpression = "BETWEEN_EXPRESSION_MISSING_UPPER_BOUND_EXPRESSION";
	String BetweenExpression_WrongType = "BETWEEN_EXPRESSION_WRONG_TYPE";
	String CaseExpression_InvalidJPAVersion = "CASE_EXPRESSION_INVALID_JPA_VERSION";
	String CaseExpression_MissingElseExpression = "CASE_EXPRESSION_MISSING_ELSE_EXPRESSION";
	String CaseExpression_MissingElseIdentifier = "CASE_EXPRESSION_MISSING_ELSE_IDENTIFIER";
	String CaseExpression_MissingEndIdentifier = "CASE_EXPRESSION_MISSING_END_IDENTIFIER";
	String CaseExpression_MissingWhenClause = "CASE_EXPRESSION_MISSING_WHEN_CLAUSE";
	String CaseExpression_WhenClausesEndWithComma = "CASE_EXPRESSION_WHEN_CLAUSES_END_WITH_COMMA";
	String CaseExpression_WhenClausesHasComma = "CASE_EXPRESSION_WHEN_CLAUSES_HAS_COMMA";
	String CoalesceExpression_InvalidExpression = "COALESCE_EXPRESSION_INVALID_EXPRESSION";
	String CoalesceExpression_InvalidJPAVersion = "COALESCE_EXPRESSION_INVALID_JPA_VERSION";
	String CoalesceExpression_MissingExpression = "COALESCE_EXPRESSION_MISSING_EXPRESSION";
	String CoalesceExpression_MissingLeftParenthesis = "COALESCE_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String CoalesceExpression_MissingRightParenthesis = "COALESCE_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String CollectionExpression_MissingExpression = "COLLECTION_EXPRESSION_MISSING_EXPRESSION";
	String CollectionMemberDeclaration_MissingCollectionValuedPathExpression = "COLLECTION_MEMBER_DECLARATION_MISSING_COLLECTION_VALUED_PATH_EXPRESSION";
	String CollectionMemberDeclaration_MissingIdentificationVariable = "COLLECTION_MEMBER_DECLARATION_MISSING_IDENTIFICATION_VARIABLE";
	String CollectionMemberDeclaration_MissingLeftParenthesis = "COLLECTION_MEMBER_DECLARATION_MISSING_LEFT_PARENTHESIS";
	String CollectionMemberDeclaration_MissingRightParenthesis = "COLLECTION_MEMBER_DECLARATION_MISSING_RIGHT_PARENTHESIS";
	String CollectionMemberExpression_Embeddable = "COLLECTION_MEMBER_EXPRESSION_EMBEDDABLE";
	String CollectionMemberExpression_MissingCollectionValuedPathExpression = "COLLECTION_MEMBER_EXPRESSION_MISSING_COLLECTION_VALUED_PATH_EXPRESSION";
	String CollectionMemberExpression_MissingEntityExpression = "COLLECTION_MEMBER_EXPRESSION_MISSING_ENTITY_EXPRESSION";
	String CollectionValuedPathExpression_NotCollectionType = "COLLECTION_VALUED_PATH_EXPRESSION_NOT_COLLECTION_TYPE";
	String CollectionValuedPathExpression_NotResolvable = "COLLECTION_VALUED_PATH_EXPRESSION_NOT_RESOLVABLE";
	String ComparisonExpression_MissingLeftExpression = "COMPARISON_EXPRESSION_MISSING_LEFT_EXPRESSION";
	String ComparisonExpression_MissingRightExpression = "COMPARISON_EXPRESSION_MISSING_RIGHT_EXPRESSION";
	String ComparisonExpression_WrongComparisonType = "COMPARISON_EXPRESSION_WRONG_COMPARISON_TYPE";
	String ConcatExpression_Expression_WrongType = "CONCAT_EXPRESSION_EXPRESSION_WRONG_TYPE";
	String ConcatExpression_InvalidExpression = "CONCAT_EXPRESSION_INVALID_EXPRESSION";
	String ConcatExpression_MissingExpression = "CONCAT_EXPRESSION_MISSING_EXPRESSION";
	String ConcatExpression_MissingLeftParenthesis = "CONCAT_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String ConcatExpression_MissingRightParenthesis = "CONCAT_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String ConstructorExpression_ConstructorItemEndsWithComma = "CONSTRUCTOR_EXPRESSION_CONSTRUCTOR_ITEM_ENDS_WITH_COMMA";
	String ConstructorExpression_ConstructorItemIsMissingComma = "CONSTRUCTOR_EXPRESSION_CONSTRUCTOR_ITEM_IS_MISSING_COMMA";
	String ConstructorExpression_MismatchedParameterTypes = "CONSTRUCTOR_EXPRESSION_MISMATCHED_PARAMETER_TYPES";
	String ConstructorExpression_MissingConstructorItem = "CONSTRUCTOR_EXPRESSION_MISSING_CONSTRUCTOR_ITEM";
	String ConstructorExpression_MissingConstructorName = "CONSTRUCTOR_EXPRESSION_MISSING_CONSTRUCTOR_NAME";
	String ConstructorExpression_MissingLeftParenthesis = "CONSTRUCTOR_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String ConstructorExpression_MissingRightParenthesis = "CONSTRUCTOR_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String ConstructorExpression_UnknownType = "CONSTRUCTOR_EXPRESSION_UNKNOWN_TYPE";
	String CountFunction_DistinctEmbeddable = "COUNT_FUNCTION_DISTINCT_EMBEDDABLE";
	String CountFunction_InvalidExpression = "COUNT_FUNCTION_INVALID_EXPRESSION";
	String CountFunction_MissingExpression = "COUNT_FUNCTION_MISSING_EXPRESSION";
	String CountFunction_MissingLeftParenthesis = "COUNT_FUNCTION_MISSING_LEFT_PARENTHESIS";
	String CountFunction_MissingRightParenthesis = "COUNT_FUNCTION_MISSING_RIGHT_PARENTHESIS";
	String DateTime_JDBCEscapeFormat_InvalidSpecification = "DATE_TIME_JDBC_ESCAPE_FORMAT_INVALID_SPECIFICATION";
	String DateTime_JDBCEscapeFormat_MissingCloseQuote = "DATE_TIME_JDBC_ESCAPE_FORMAT_MISSING_CLOSE_QUOTE";
	String DateTime_JDBCEscapeFormat_MissingOpenQuote = "DATE_TIME_JDBC_ESCAPE_FORMAT_MISSING_OPEN_QUOTE";
	String DateTime_JDBCEscapeFormat_MissingRightCurlyBrace = "DATE_TIME_JDBC_ESCAPE_FORMAT_MISSING_RIGHT_CURLY_BRACE";
	String DeleteClause_FromMissing = "DELETE_CLAUSE_FROM_MISSING";
	String DeleteClause_MultipleRangeVariableDeclaration = "DELETE_CLAUSE_MULTIPLE_RANGE_VARIABLE_DECLARATION";
	String DeleteClause_RangeVariableDeclarationMalformed = "DELETE_CLAUSE_RANGE_VARIABLE_DECLARATION_MALFORMED";
	String DeleteClause_RangeVariableDeclarationMissing = "DELETE_CLAUSE_RANGE_VARIABLE_DECLARATION_MISSING";
	String DivisionExpression_LeftExpression_WrongType = "DIVISION_EXPRESSION_LEFT_EXPRESSION_WRONG_TYPE";
	String DivisionExpression_RightExpression_WrongType = "DIVISION_EXPRESSION_RIGHT_EXPRESSION_WRONG_TYPE";
	String EmptyCollectionComparisonExpression_MissingExpression = "EMPTY_COLLECTION_COMPARISON_EXPRESSION_MISSING_EXPRESSION";
	String EncapsulatedIdentificationVariableExpression_NotMapValued = "ENCAPSULATED_IDENTIFICATION_VARIABLE_EXPRESSION_NOT_MAP_VALUED";
	String EntityTypeLiteral_NotResolvable = "ENTITY_TYPE_LITERAL_NOT_RESOLVABLE";
	String EntryExpression_InvalidExpression = "ENTRY_EXPRESSION_INVALID_EXPRESSION";
	String EntryExpression_InvalidJPAVersion = "ENTRY_EXPRESSION_INVALID_JPA_VERSION";
	String EntryExpression_MissingExpression = "ENTRY_EXPRESSION_MISSING_EXPRESSION";
	String EntryExpression_MissingLeftParenthesis = "ENTRY_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String EntryExpression_MissingRightParenthesis = "ENTRY_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String ExistsExpression_InvalidExpression = "EXISTS_EXPRESSION_INVALID_EXPRESSION";
	String ExistsExpression_MissingExpression = "EXISTS_EXPRESSION_MISSING_EXPRESSION";
	String ExistsExpression_MissingLeftParenthesis = "EXISTS_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String ExistsExpression_MissingRightParenthesis = "EXISTS_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String FuncExpression_InvalidJPAPlatform = "FUNC_EXPRESSION_INVALID_JPA_PLATFORM";
	String FuncExpression_MissingFunctionName = "FUNC_EXPRESSION_MISSING_FUNCTION_NAME";
	String FuncExpression_MissingLeftParenthesis = "FUNC_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String FuncExpression_MissingRightParenthesis = "FUNC_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String GroupByClause_GroupByItemEndsWithComma = "GROUP_BY_CLAUSE_GROUP_BY_ITEM_ENDS_WITH_COMMA";
	String GroupByClause_GroupByItemIsMissingComma = "GROUP_BY_CLAUSE_GROUP_BY_ITEM_IS_MISSING_COMMA";
	String GroupByClause_GroupByItemMissing = "GROUP_BY_CLAUSE_GROUP_BY_ITEM_MISSING";
	String HavingClause_InvalidConditionalExpression = "HAVING_CLAUSE_INVALID_CONDITIONAL_EXPRESSION";
	String HavingClause_MissingConditionalExpression = "HAVING_CLAUSE_MISSING_CONDITIONAL_EXPRESSION";
	String HermesParser_GrammarValidator_ErrorMessage = "HERMES_PARSER_GRAMMAR_VALIDATOR_ERROR_MESSAGE";
	String HermesParser_SemanticValidator_ErrorMessage = "HERMES_PARSER_SEMANTIC_VALIDATOR_ERROR_MESSAGE";
	String IdentificationVariable_EntityName = "IDENTIFICATION_VARIABLE_ENTITY_NAME";
	String IdentificationVariable_Invalid_Duplicate = "IDENTIFICATION_VARIABLE_INVALID_DUPLICATE";
	String IdentificationVariable_Invalid_JavaIdentifier = "IDENTIFICATION_VARIABLE_INVALID_JAVA_IDENTIFIER";
	String IdentificationVariable_Invalid_NotDeclared = "IDENTIFICATION_VARIABLE_INVALID_NOT_DECLARED";
	String IdentificationVariable_Invalid_ReservedWord = "IDENTIFICATION_VARIABLE_INVALID_RESERVED_WORD";
	String IdentificationVariableDeclaration_MissingRangeVariableDeclaration = "IDENTIFICATION_VARIABLE_DECLARATION_MISSING_RANGE_VARIABLE_DECLARATION";
	String IndexExpression_InvalidExpression = "INDEX_EXPRESSION_INVALID_EXPRESSION";
	String IndexExpression_InvalidJPAVersion = "INDEX_EXPRESSION_INVALID_JPA_VERSION";
	String IndexExpression_MissingExpression = "INDEX_EXPRESSION_MISSING_EXPRESSION";
	String IndexExpression_MissingLeftParenthesis = "INDEX_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String IndexExpression_MissingRightParenthesis = "INDEX_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String IndexExpression_WrongVariable = "INDEX_EXPRESSION_WRONG_VARIABLE";
	String InExpression_InItemEndsWithComma = "IN_EXPRESSION_IN_ITEM_ENDS_WITH_COMMA";
	String InExpression_InItemIsMissingComma = "IN_EXPRESSION_IN_ITEM_IS_MISSING_COMMA";
	String InExpression_MalformedExpression = "IN_EXPRESSION_MALFORMED_EXPRESSION";
	String InExpression_MissingExpression = "IN_EXPRESSION_MISSING_EXPRESSION";
	String InExpression_MissingInItems = "IN_EXPRESSION_MISSING_IN_ITEMS";
	String InExpression_MissingLeftParenthesis = "IN_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String InExpression_MissingRightParenthesis = "IN_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String InputParameter_JavaIdentifier = "INPUT_PARAMETER_JAVA_IDENTIFIER";
	String InputParameter_MissingParameter = "INPUT_PARAMETER_MISSING_PARAMETER";
	String InputParameter_Mixture = "INPUT_PARAMETER_MIXTURE";
	String InputParameter_NotInteger = "INPUT_PARAMETER_NOT_INTEGER";
	String InputParameter_SmallerThanOne = "INPUT_PARAMETER_SMALLER_THAN_ONE";
	String InputParameter_WrongClauseDeclaration = "INPUT_PARAMETER_WRONG_CLAUSE_DECLARATION";
	String Join_MissingIdentificationVariable = "JOIN_MISSING_IDENTIFICATION_VARIABLE";
	String Join_MissingJoinAssociationPath = "JOIN_MISSING_JOIN_ASSOCIATION_PATH";
	String JoinFetch_MissingJoinAssociationPath = "JOIN_FETCH_MISSING_JOIN_ASSOCIATION_PATH";
	String JoinFetch_WrongClauseDeclaration = "JOIN_FETCH_WRONG_CLAUSE_DECLARATION";
	String JPQLExpression_InvalidQuery = "JPQL_EXPRESSION_INVALID_QUERY";
	String JPQLExpression_UnknownEnding = "JPQL_EXPRESSION_UNKNOWN_ENDING";
	String KeyExpression_InvalidExpression = "KEY_EXPRESSION_INVALID_EXPRESSION";
	String KeyExpression_InvalidJPAVersion = "KEY_EXPRESSION_INVALID_JPA_VERSION";
	String KeyExpression_MissingExpression = "KEY_EXPRESSION_MISSING_EXPRESSION";
	String KeyExpression_MissingLeftParenthesis = "KEY_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String KeyExpression_MissingRightParenthesis = "KEY_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String LengthExpression_InvalidExpression = "LENGTH_EXPRESSION_INVALID_EXPRESSION";
	String LengthExpression_MissingExpression = "LENGTH_EXPRESSION_MISSING_EXPRESSION";
	String LengthExpression_MissingLeftParenthesis = "LENGTH_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String LengthExpression_MissingRightParenthesis = "LENGTH_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String LengthExpression_WrongType = "LENGTH_EXPRESSION_WRONG_TYPE";
	String LikeExpression_InvalidEscapeCharacter = "LIKE_EXPRESSION_INVALID_ESCAPE_CHARACTER";
	String LikeExpression_MissingEscapeCharacter = "LIKE_EXPRESSION_MISSING_ESCAPE_CHARACTER";
	String LikeExpression_MissingPatternValue = "LIKE_EXPRESSION_MISSING_PATTERN_VALUE";
	String LikeExpression_MissingStringExpression = "LIKE_EXPRESSION_MISSING_STRING_EXPRESSION";
	String LocateExpression_FirstExpression_WrongType = "LOCATE_EXPRESSION_FIRST_EXPRESSION_WRONG_TYPE";
	String LocateExpression_InvalidFirstExpression = "LOCATE_EXPRESSION_INVALID_FIRST_EXPRESSION";
	String LocateExpression_InvalidSecondExpression = "LOCATE_EXPRESSION_INVALID_SECOND_EXPRESSION";
	String LocateExpression_InvalidThirdExpression = "LOCATE_EXPRESSION_INVALID_THIRD_EXPRESSION";
	String LocateExpression_MissingFirstComma = "LOCATE_EXPRESSION_MISSING_FIRST_COMMA";
	String LocateExpression_MissingFirstExpression = "LOCATE_EXPRESSION_MISSING_FIRST_EXPRESSION";
	String LocateExpression_MissingLeftParenthesis = "LOCATE_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String LocateExpression_MissingRightParenthesis = "LOCATE_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String LocateExpression_MissingSecondComma = "LOCATE_EXPRESSION_MISSING_SECOND_COMMA";
	String LocateExpression_MissingSecondExpression = "LOCATE_EXPRESSION_MISSING_SECOND_EXPRESSION";
	String LocateExpression_MissingThirdExpression = "LOCATE_EXPRESSION_MISSING_THIRD_EXPRESSION";
	String LocateExpression_SecondExpression_WrongType = "LOCATE_EXPRESSION_SECOND_EXPRESSION_WRONG_TYPE";
	String LocateExpression_ThirdExpression_WrongType = "LOCATE_EXPRESSION_THIRD_EXPRESSION_WRONG_TYPE";
	String LogicalExpression_InvalidLeftExpression = "LOGICAL_EXPRESSION_INVALID_LEFT_EXPRESSION";
	String LogicalExpression_InvalidRightExpression = "LOGICAL_EXPRESSION_INVALID_RIGHT_EXPRESSION";
	String LogicalExpression_MissingLeftExpression = "LOGICAL_EXPRESSION_MISSING_LEFT_EXPRESSION";
	String LogicalExpression_MissingRightExpression = "LOGICAL_EXPRESSION_MISSING_RIGHT_EXPRESSION";
	String LowerExpression_InvalidExpression = "LOWER_EXPRESSION_INVALID_EXPRESSION";
	String LowerExpression_MissingExpression = "LOWER_EXPRESSION_MISSING_EXPRESSION";
	String LowerExpression_MissingLeftParenthesis = "LOWER_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String LowerExpression_MissingRightParenthesis = "LOWER_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String LowerExpression_WrongType = "LOWER_EXPRESSION_WRONG_TYPE";
	String MaxFunction_InvalidExpression = "MAX_FUNCTION_INVALID_EXPRESSION";
	String MaxFunction_MissingExpression = "MAX_FUNCTION_MISSING_EXPRESSION";
	String MaxFunction_MissingLeftParenthesis = "MAX_FUNCTION_MISSING_LEFT_PARENTHESIS";
	String MaxFunction_MissingRightParenthesis = "MAX_FUNCTION_MISSING_RIGHT_PARENTHESIS";
	String MinFunction_InvalidExpression = "MIN_FUNCTION_INVALID_EXPRESSION";
	String MinFunction_MissingExpression = "MIN_FUNCTION_MISSING_EXPRESSION";
	String MinFunction_MissingLeftParenthesis = "MIN_FUNCTION_MISSING_LEFT_PARENTHESIS";
	String MinFunction_MissingRightParenthesis = "MIN_FUNCTION_MISSING_RIGHT_PARENTHESIS";
	String ModExpression_FirstExpression_WrongType = "MOD_EXPRESSION_FIRST_EXPRESSION_WRONG_TYPE";
	String ModExpression_InvalidFirstExpression = "MOD_EXPRESSION_INVALID_FIRST_EXPRESSION";
	String ModExpression_InvalidSecondParenthesis = "MOD_EXPRESSION_INVALID_SECOND_PARENTHESIS";
	String ModExpression_MissingComma = "MOD_EXPRESSION_MISSING_COMMA";
	String ModExpression_MissingFirstExpression = "MOD_EXPRESSION_MISSING_FIRST_EXPRESSION";
	String ModExpression_MissingLeftParenthesis = "MOD_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String ModExpression_MissingRightParenthesis = "MOD_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String ModExpression_MissingSecondExpression = "MOD_EXPRESSION_MISSING_SECOND_EXPRESSION";
	String ModExpression_SecondExpression_WrongType = "MOD_EXPRESSION_SECOND_EXPRESSION_WRONG_TYPE";
	String MultiplicationExpression_LeftExpression_WrongType = "MULTIPLICATION_EXPRESSION_LEFT_EXPRESSION_WRONG_TYPE";
	String MultiplicationExpression_RightExpression_WrongType = "MULTIPLICATION_EXPRESSION_RIGHT_EXPRESSION_WRONG_TYPE";
	String NotExpression_MissingExpression = "NOT_EXPRESSION_MISSING_EXPRESSION";
	String NotExpression_WrongType = "NOT_EXPRESSION_WRONG_TYPE";
	String NullComparisonExpression_InvalidType = "NULL_COMPARISON_EXPRESSION_INVALID_TYPE";
	String NullComparisonExpression_MissingExpression = "NULL_COMPARISON_EXPRESSION_MISSING_EXPRESSION";
	String NullIfExpression_InvalidFirstExpression = "NULLIF_EXPRESSION_INVALID_FIRST_EXPRESSION";
	String NullIfExpression_InvalidJPAVersion = "NULLIF_EXPRESSION_INVALID_JPA_VERSION";
	String NullIfExpression_InvalidSecondExpression = "NULLIF_EXPRESSION_INVALID_SECOND_EXPRESSION";
	String NullIfExpression_MissingComma = "NULLIF_EXPRESSION_MISSING_COMMA";
	String NullIfExpression_MissingFirstExpression = "NULLIF_EXPRESSION_MISSING_FIRST_EXPRESSION";
	String NullIfExpression_MissingLeftParenthesis = "NULLIF_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String NullIfExpression_MissingRightParenthesis = "NULLIF_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String NullIfExpression_MissingSecondExpression = "NULLIF_EXPRESSION_MISSING_SECOND_EXPRESSION";
	String NumericLiteral_Invalid = "NUMERIC_LITERAL_INVALID";
	String ObjectExpression_InvalidExpression = "OBJECT_EXPRESSION_INVALID_EXPRESSION";
	String ObjectExpression_MissingExpression = "OBJECT_EXPRESSION_MISSING_EXPRESSION";
	String ObjectExpression_MissingLeftParenthesis = "OBJECT_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String ObjectExpression_MissingRightParenthesis = "OBJECT_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String OrderByClause_OrderByItemEndsWithComma = "ORDER_BY_CLAUSE_ORDER_BY_ITEM_ENDS_WITH_COMMA";
	String OrderByClause_OrderByItemIsMissingComma = "ORDER_BY_CLAUSE_ORDER_BY_ITEM_IS_MISSING_COMMA";
	String OrderByClause_OrderByItemMissing = "ORDER_BY_CLAUSE_ORDER_BY_ITEM_MISSING";
	String OrderByItem_InvalidPath = "ORDER_BY_ITEM_INVALID_PATH";
	String OrderByItem_MissingStateFieldPathExpression = "ORDER_BY_ITEM_MISSING_STATE_FIELD_PATH_EXPRESSION";
	String PathExpression_NotRelationshipMapping = "PATH_EXPRESSION_NOT_RELATIONSHIP_MAPPING";
	String RangeVariableDeclaration_MissingAbstractSchemaName = "RANGE_VARIABLE_DECLARATION_MISSING_ABSTRACT_SCHEMA_NAME";
	String RangeVariableDeclaration_MissingIdentificationVariable = "RANGE_VARIABLE_DECLARATION_MISSING_IDENTIFICATION_VARIABLE";
	String ResultVariable_InvalidJPAVersion = "RESULT_VARIABLE_INVALID_JPA_VERSION";
	String ResultVariable_MissingResultVariable = "RESULT_VARIABLE_MISSING_RESULT_VARIABLE";
	String ResultVariable_MissingSelectExpression = "RESULT_VARIABLE_MISSING_SELECT_EXPRESSION";
	String SelectStatement_SelectClauseHasNonAggregateFunctions = "SELECT_STATEMENT_SELECT_CLAUSE_HAS_NON_AGGREGATE_FUNCTIONS";
	String SimpleSelectClause_NotSingleExpression = "SIMPLE_SELECT_CLAUSE_NOT_SINGLE_EXPRESSION";
	String SimpleSelectStatement_InvalidLocation = "SIMPLE_SELECT_STATEMENT_INVALID_LOCATION";
	String SizeExpression_InvalidExpression = "SIZE_EXPRESSION_INVALID_EXPRESSION";
	String SizeExpression_MissingExpression = "SIZE_EXPRESSION_MISSING_EXPRESSION";
	String SizeExpression_MissingLeftParenthesis = "SIZE_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String SizeExpression_MissingRightParenthesis = "SIZE_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String SqrtExpression_InvalidExpression = "SQRT_EXPRESSION_INVALID_EXPRESSION";
	String SqrtExpression_MissingExpression = "SQRT_EXPRESSION_MISSING_EXPRESSION";
	String SqrtExpression_MissingLeftParenthesis = "SQRT_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String SqrtExpression_MissingRightParenthesis = "SQRT_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String SqrtExpression_WrongType = "SQRT_EXPRESSION_WRONG_TYPE";
	String StateFieldPathExpression_AssociationField = "STATE_FIELD_PATH_EXPRESSION_ASSOCIATION_FIELD";
	String StateFieldPathExpression_CollectionType = "STATE_FIELD_PATH_EXPRESSION_COLLECTION_TYPE";
	String StateFieldPathExpression_InvalidEnumConstant = "STATE_FIELD_PATH_EXPRESSION_INVALID_ENUM_CONSTANT";
	String StateFieldPathExpression_NoMapping = "STATE_FIELD_PATH_EXPRESSION_NO_MAPPING";
	String StateFieldPathExpression_NotResolvable = "STATE_FIELD_PATH_EXPRESSION_NOT_RESOLVABLE";
	String StringLiteral_MissingClosingQuote = "STRING_LITERAL_MISSING_CLOSING_QUOTE";
	String SubExpression_MissingExpression = "SUB_EXPRESSION_MISSING_EXPRESSION";
	String SubExpression_MissingRightParenthesis = "SUB_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String SubstringExpression_FirstExpression_WrongType = "SUBSTRING_EXPRESSION_FIRST_EXPRESSION_WRONG_TYPE";
	String SubstringExpression_InvalidFirstExpression = "SUBSTRING_EXPRESSION_INVALID_FIRST_EXPRESSION";
	String SubstringExpression_InvalidSecondExpression = "SUBSTRING_EXPRESSION_INVALID_SECOND_EXPRESSION";
	String SubstringExpression_InvalidThirdExpression = "SUBSTRING_EXPRESSION_INVALID_THIRD_EXPRESSION";
	String SubstringExpression_MissingFirstComma = "SUBSTRING_EXPRESSION_MISSING_FIRST_COMMA";
	String SubstringExpression_MissingFirstExpression = "SUBSTRING_EXPRESSION_MISSING_FIRST_EXPRESSION";
	String SubstringExpression_MissingLeftParenthesis = "SUBSTRING_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String SubstringExpression_MissingRightParenthesis = "SUBSTRING_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String SubstringExpression_MissingSecondComma = "SUBSTRING_EXPRESSION_MISSING_SECOND_COMMA";
	String SubstringExpression_MissingSecondExpression = "SUBSTRING_EXPRESSION_MISSING_SECOND_EXPRESSION";
	String SubstringExpression_MissingThirdExpression = "SUBSTRING_EXPRESSION_MISSING_THIRD_EXPRESSION";
	String SubstringExpression_SecondExpression_WrongType = "SUBSTRING_EXPRESSION_SECOND_EXPRESSION_WRONG_TYPE";
	String SubstringExpression_ThirdExpression_WrongType = "SUBSTRING_EXPRESSION_THIRD_EXPRESSION_WRONG_TYPE";
	String SubtractionExpression_LeftExpression_WrongType = "SUBTRACTION_EXPRESSION_LEFT_EXPRESSION_WRONG_TYPE";
	String SubtractionExpression_RightExpression_WrongType = "SUBTRACTION_EXPRESSION_RIGHT_EXPRESSION_WRONG_TYPE";
	String SumFunction_InvalidExpression = "SUM_FUNCTION_INVALID_EXPRESSION";
	String SumFunction_MissingExpression = "SUM_FUNCTION_MISSING_EXPRESSION";
	String SumFunction_MissingLeftParenthesis = "SUM_FUNCTION_MISSING_LEFT_PARENTHESIS";
	String SumFunction_MissingRightParenthesis = "SUM_FUNCTION_MISSING_RIGHT_PARENTHESIS";
	String SumFunction_WrongType = "SUM_FUNCTION_WRONG_TYPE";
	String TreatExpression_InvalidJPAPlatform = "TREAT_EXPRESSION_INVALID_JPA_PLATFORM";
	String TrimExpression_InvalidExpression = "TRIM_EXPRESSION_INVALID_EXPRESSION";
	String TrimExpression_InvalidTrimCharacter = "TRIM_EXPRESSION_INVALID_TRIM_CHARACTER";
	String TrimExpression_MissingExpression = "TRIM_EXPRESSION_MISSING_EXPRESSION";
	String TrimExpression_MissingLeftParenthesis = "TRIM_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String TrimExpression_MissingRightParenthesis = "TRIM_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String TrimExpression_NotSingleStringLiteral = "TRIM_EXPRESSION_NOT_SINGLE_STRING_LITERAL";
	String TypeExpression_InvalidExpression = "TYPE_EXPRESSION_INVALID_EXPRESSION";
	String TypeExpression_InvalidJPAVersion = "TYPE_EXPRESSION_INVALID_JPA_VERSION";
	String TypeExpression_MissingExpression = "TYPE_EXPRESSION_MISSING_EXPRESSION";
	String TypeExpression_MissingLeftParenthesis = "TYPE_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String TypeExpression_MissingRightParenthesis = "TYPE_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String UpdateClause_MissingRangeVariableDeclaration = "UPDATE_CLAUSE_MISSING_RANGE_VARIABLE_DECLARATION";
	String UpdateClause_MissingSet = "UPDATE_CLAUSE_MISSING_SET";
	String UpdateClause_MissingUpdateItems = "UPDATE_CLAUSE_MISSING_UPDATE_ITEMS";
	String UpdateClause_UpdateItemEndsWithComma = "UPDATE_CLAUSE_UPDATE_ITEM_ENDS_WITH_COMMA";
	String UpdateClause_UpdateItemIsMissingComma = "UPDATE_CLAUSE_UPDATE_ITEM_IS_MISSING_COMMA";
	String UpdateItem_MissingEqualSign = "UPDATE_ITEM_MISSING_EQUAL_SIGN";
	String UpdateItem_MissingNewValue = "UPDATE_ITEM_MISSING_NEW_VALUE";
	String UpdateItem_MissingStateFieldPathExpression = "UPDATE_ITEM_MISSING_STATE_FIELD_PATH_EXPRESSION";
	String UpdateItem_NotAssignable = "UPDATE_ITEM_NOT_ASSIGNABLE";
	String UpdateItem_NotResolvable = "UPDATE_ITEM_NOT_RESOLVABLE";
	String UpdateItem_NullNotAssignableToPrimitive = "UPDATE_ITEM_NULL_NOT_ASSIGNABLE_TO_PRIMITIVE";
	String UpperExpression_InvalidExpression = "UPPER_EXPRESSION_INVALID_EXPRESSION";
	String UpperExpression_MissingExpression = "UPPER_EXPRESSION_MISSING_EXPRESSION";
	String UpperExpression_MissingLeftParenthesis = "UPPER_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String UpperExpression_MissingRightParenthesis = "UPPER_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String UpperExpression_WrongType = "UPPER_EXPRESSION_WRONG_TYPE";
	String ValueExpression_InvalidExpression = "VALUE_EXPRESSION_INVALID_EXPRESSION";
	String ValueExpression_InvalidJPAVersion = "VALUE_EXPRESSION_INVALID_JPA_VERSION";
	String ValueExpression_MissingExpression = "VALUE_EXPRESSION_MISSING_EXPRESSION";
	String ValueExpression_MissingLeftParenthesis = "VALUE_EXPRESSION_MISSING_LEFT_PARENTHESIS";
	String ValueExpression_MissingRightParenthesis = "VALUE_EXPRESSION_MISSING_RIGHT_PARENTHESIS";
	String WhenClause_MissingThenExpression = "WHEN_CLAUSE_MISSING_THEN_EXPRESSION";
	String WhenClause_MissingThenIdentifier = "WHEN_CLAUSE_MISSING_THEN_IDENTIFIER";
	String WhenClause_MissingWhenExpression = "WHEN_CLAUSE_MISSING_WHEN_EXPRESSION";
	String WhereClause_InvalidConditionalExpression = "WHERE_CLAUSE_INVALID_CONDITIONAL_EXPRESSION";
	String WhereClause_MissingConditionalExpression = "WHERE_CLAUSE_MISSING_CONDITIONAL_EXPRESSION";
}